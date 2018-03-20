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
  private var viewFactory: CyaneaViewFactory? = null
  private var decorators: Array<Decorator>? = null

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
    var view: View? = null
    for (prefix in CLASS_PREFIX_LIST) {
      try {
        view = createView(name, prefix, attrs)
        if (view != null) {
          break
        }
      } catch (ignored: ClassNotFoundException) {
      }
    }
    if (view == null) {
      // In this case we want to let the base class take a crack at it.
      view = super.onCreateView(name, attrs)
    }
    return processView(view, attrs)
  }

  private fun processView(onCreateView: View?, attrs: AttributeSet): View? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun createCustomView(parent: View, view: View?, name: String, context: Context, attrs: AttributeSet): View? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    if (method != null) {
      val factory2 = context as? LayoutInflater.Factory2 ?: return
      val factory = PrivateWrapperFactory2(this, factory2)
      try {
        method.invoke(this, factory)
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

    override fun onCreateView(parent: View, name: String, context: Context, attrs: AttributeSet): View? {
      return inflater.processView(factory.onCreateView(parent, name, context, attrs), attrs)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
      return inflater.processView(factory.onCreateView(name, context, attrs), attrs)
    }

  }

  private class PrivateWrapperFactory2 internal constructor(
      inflater: CyaneaLayoutInflater, factory: LayoutInflater.Factory2)
    : WrapperFactory2(inflater, factory) {

    override fun onCreateView(parent: View, name: String, context: Context, attrs: AttributeSet): View? {
      val view = factory.onCreateView(parent, name, context, attrs)
      return inflater.processView(inflater.createCustomView(parent, view, name, context, attrs), attrs)
    }

  }

  companion object {
    private val CLASS_PREFIX_LIST = arrayOf("android.widget.", "android.webkit.")
  }

}