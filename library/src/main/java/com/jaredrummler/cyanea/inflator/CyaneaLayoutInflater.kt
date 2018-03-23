package com.jaredrummler.cyanea.inflator

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaredrummler.cyanea.inflator.decor.Decorator
import com.jaredrummler.cyanea.utils.Reflection

class CyaneaLayoutInflater : LayoutInflater {
  constructor(context: ContextWrapper) : this(from(context.baseContext), context)
  constructor(inflater: LayoutInflater, context: Context) : this(inflater, context, false)
  constructor(inflater: LayoutInflater, context: Context, cloned: Boolean) : super(inflater, context) {
    if (!cloned) {
      if (factory2 != null && factory2 !is WrapperFactory2) {
        factory2 = factory2
      }
      if (factory != null && factory !is WrapperFactory) {
        factory = factory
      }
    }
  }

  private var setPrivateFactory: Boolean = false
  var viewFactory: CyaneaViewFactory? = null
  var decorators: Array<Decorator>? = null

  override fun cloneInContext(newContext: Context): LayoutInflater {
    val inflator = CyaneaLayoutInflater(this, newContext, true)
    inflator.viewFactory = viewFactory
    inflator.decorators = decorators
    return inflator
  }

  override fun inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View {
    setPrivateFactoryInternal()
    return super.inflate(resource, root, attachToRoot)
  }

  override fun setFactory(factory: LayoutInflater.Factory) {
    if (factory !is WrapperFactory) {
      super.setFactory(WrapperFactory(this, factory))
    } else {
      super.setFactory(factory)
    }
  }

  override fun setFactory2(factory2: LayoutInflater.Factory2) {
    if (factory2 !is WrapperFactory2) {
      super.setFactory2(WrapperFactory2(this, factory2))
    } else {
      super.setFactory2(factory2)
    }
  }

  @Throws(ClassNotFoundException::class)
  override fun onCreateView(name: String, attrs: AttributeSet): View? {
    for (prefix in CLASS_PREFIX_LIST) {
      try {
        createView(name, prefix, attrs)?.let { return processView(it, attrs) }
      } catch (ignored: ClassNotFoundException) {
      }
    }
    super.onCreateView(name, attrs)?.let { return processView(it, attrs) } ?: return null
  }

  /**
   * Method to dispatch our view and attributes to the [CyaneaViewFactory].
   * Called immediately after [onCreateView][CyaneaLayoutInflater.onCreateView]
   *
   * @param view
   *     The view being inflated
   * @param attrs
   *     The attributes for the view
   * @return Newly created view
   */
  private fun processView(view: View?, attrs: AttributeSet): View? {
    if (view == null) return null
    decorators?.forEach { it.apply(view, attrs) }
    viewFactory?.let { return it.onViewCreated(view, attrs) } ?: return view
  }

  /**
   * Method to inflate custom layouts that haven't been handled else where. If this fails it will fall back
   * through to the PhoneLayoutInflater method of inflating custom views where we will NOT have a hook into.
   *
   * @param parent
   *     parent view
   * @param view
   *     view if it has been inflated by this point, if this is not null this method just returns this value.
   * @param name
   *     name of the thing to inflate.
   * @param viewContext
   *     Context to inflate by if parent is null
   * @param attrs
   *     Attr for this view which we can steal fontPath from too.
   * @return view or the View we inflate in here.
   */
  private fun createCustomView(parent: View?, view: View?, name: String, context: Context, attrs: AttributeSet): View? {
    // I by no means advise anyone to do this normally, but Google has locked down access to the createView() method,
    // so we never get a callback with attributes at the end of the createViewFromTag chain (which would solve all
    // this unnecessary rubbish). We at the very least try to optimise this as much as possible. We only call for
    // customViews (As they are the ones that never go through onCreateView(...)). We also maintain the Field
    // reference and make it accessible which will make a pretty significant difference to performance on Android 4.0+.

    if (view == null && name.indexOf('.') > -1) {
      val field = Reflection.getField(LayoutInflater::class.java, "mConstructorArgs")
      field?.let {
        if (!it.isAccessible) it.isAccessible = true
        val constructionArgs = it.get(this) as? Array<Any> ?: return view
        // The LayoutInflater actually finds out the correct context to use.
        // We just need to set it on the mConstructor for the internal method.
        val lastContext = constructionArgs[0]
        constructionArgs[0] = context
        it.set(this, constructionArgs)
        try {
          createView(name, null, attrs)?.let { v -> return v }
        } catch (e: ClassNotFoundException) {
        } finally {
          constructionArgs[0] = lastContext
          it.set(this, constructionArgs)
        }
      }
    }
    return view
  }

  private fun setPrivateFactoryInternal() {
    if (setPrivateFactory) {
      return
    }

    if (context !is LayoutInflater.Factory2) {
      setPrivateFactory = true
      return
    }

    val method = Reflection.getMethod(LayoutInflater::class.java, "setPrivateFactory", Factory2::class.java)
    method?.let {
      val factory2 = context as? LayoutInflater.Factory2 ?: return
      val factory = PrivateWrapperFactory2(this, factory2)
      try {
        it.invoke(this, factory)
      } catch (ignored: Exception) {
      }
    }

    setPrivateFactory = true
  }

  private class WrapperFactory internal constructor(
      private val inflater: CyaneaLayoutInflater,
      private val factory: LayoutInflater.Factory)
    : LayoutInflater.Factory {

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
      return inflater.processView(factory.onCreateView(name, context, attrs), attrs)
    }

  }

  private open class WrapperFactory2 internal constructor(
      internal val inflater: CyaneaLayoutInflater,
      internal val factory: LayoutInflater.Factory2)
    : LayoutInflater.Factory2 {

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
      return inflater.processView(factory.onCreateView(parent, name, context, attrs), attrs)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
      return inflater.processView(factory.onCreateView(name, context, attrs), attrs)
    }

  }

  private class PrivateWrapperFactory2 internal constructor(
      inflater: CyaneaLayoutInflater, factory: LayoutInflater.Factory2)
    : WrapperFactory2(inflater, factory) {

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
      val view = factory.onCreateView(parent, name, context, attrs)
      return inflater.processView(inflater.createCustomView(parent, view, name, context, attrs), attrs)
    }

  }

  companion object {
    private val CLASS_PREFIX_LIST = arrayOf("android.widget.", "android.webkit.")
  }

}