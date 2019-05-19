/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.cyanea.inflator

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator
import com.jaredrummler.cyanea.utils.Reflection

/**
 * A [LayoutInflater] that intercepts views on creation.
 *
 * Based on the LayoutInflater by Christopher Jenkins.
 * See: https://github.com/chrisjenx/Calligraphy
 */
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
  var decorators: Array<CyaneaDecorator>? = null

  override fun cloneInContext(newContext: Context): LayoutInflater {
    val inflater = CyaneaLayoutInflater(this, newContext, true)
    inflater.viewFactory = viewFactory
    inflater.decorators = decorators
    return inflater
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
   * @param view
   *     view if it has been inflated by this point, if this is not null this method just returns this value.
   * @param name
   *     name of the thing to inflate.
   * @param context
   *     Context to inflate by if parent is null
   * @param attrs
   *     Attr for this view which we can steal fontPath from too.
   * @return view or the View we inflate in here.
   */
  private fun createCustomView(view: View?, name: String, context: Context, attrs: AttributeSet): View? {
    // I by no means advise anyone to do this normally, but Google has locked down access to the createView() method,
    // so we never get a callback with attributes at the end of the createViewFromTag chain (which would solve all
    // this unnecessary rubbish). We at the very least try to optimise this as much as possible. We only call for
    // customViews (As they are the ones that never go through onCreateView(...)). We also maintain the Field
    // reference and make it accessible which will make a pretty significant difference to performance on Android 4.0+.
    if (view == null && name.indexOf('.') > -1) {
      Reflection.getField(LayoutInflater::class.java, "mConstructorArgs")?.let { field ->
        if (!field.isAccessible) field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val constructionArgs = field.get(this) as? Array<Any> ?: return null
        // The LayoutInflater actually finds out the correct context to use.
        // We just need to set it on the mConstructor for the internal method.
        val lastContext = constructionArgs[0]
        constructionArgs[0] = context
        field.set(this, constructionArgs)
        try {
          createView(name, null, attrs)?.let { v -> return v }
        } catch (e: ClassNotFoundException) {
        } finally {
          constructionArgs[0] = lastContext
          field.set(this, constructionArgs)
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

  private fun createViewFromDelegate(parent: View?, name: String, context: Context, attrs: AttributeSet): View? =
      inflationDelegate?.createView(parent, name, context, attrs)

  private class WrapperFactory internal constructor(
    private val inflater: CyaneaLayoutInflater,
    private val factory: LayoutInflater.Factory
  ) : LayoutInflater.Factory {

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? =
        processView(inflater.createViewFromDelegate(null, name, context, attrs)
            ?: factory.onCreateView(name, context, attrs), attrs)

    protected fun processView(view: View?, attrs: AttributeSet): View? = inflater.processView(view, attrs)
  }

  private open class WrapperFactory2 internal constructor(
    internal val inflater: CyaneaLayoutInflater,
    internal val factory: LayoutInflater.Factory2
  ) : LayoutInflater.Factory2 {

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? =
        processView(inflater.createViewFromDelegate(parent, name, context, attrs)
            ?: factory.onCreateView(parent, name, context, attrs), attrs)

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? =
        processView(inflater.createViewFromDelegate(null, name, context, attrs)
            ?: factory.onCreateView(name, context, attrs), attrs)

    protected fun processView(view: View?, attrs: AttributeSet): View? = inflater.processView(view, attrs)
  }

  private class PrivateWrapperFactory2 internal constructor(
    inflater: CyaneaLayoutInflater,
    factory: LayoutInflater.Factory2
  ) : WrapperFactory2(inflater, factory) {

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? =
        factory.onCreateView(parent, name, context, attrs).let { view ->
          processView(
              inflater.createViewFromDelegate(view, name, context, attrs)
                  ?: inflater.createCustomView(view, name, context, attrs), attrs
          )
        }
  }

  companion object {
    private val CLASS_PREFIX_LIST = arrayOf("android.widget.", "android.webkit.", "android.app.")
    internal var inflationDelegate: CyaneaInflationDelegate? = null
  }
}
