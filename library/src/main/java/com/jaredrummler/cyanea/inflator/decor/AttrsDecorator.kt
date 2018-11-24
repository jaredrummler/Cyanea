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

package com.jaredrummler.cyanea.inflator.decor

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * A base class for a decorator that transform certain View subtypes with certain attributes. Useful when you want to
 * extend standard layout inflation to add your own attributes to system widgets.
 * If a view with type [AttrsDecorator.getType] is inflated and it has one of the attributes returned in
 * [AttrsDecorator.styleable] method then [AttrsDecorator.apply] will be invoked for that view.
 *
 * @param <T>
 *     The type or parent type of View that this decorator applies to.
 */
abstract class AttrsDecorator<T : View> : CyaneaDecorator {

  override fun apply(view: View, attrs: AttributeSet) {
    if (getType().isAssignableFrom(view.javaClass)) {
      val values = obtainAttributes(view.context, attrs)
      values?.let {
        try {
          for (i in 0 until values.length()) {
            val buf = TypedValue()
            if (it.hasValue(i) && it.getValue(i, buf)) {
              @Suppress("UNCHECKED_CAST")
              apply(view as T, it)
              break
            }
          }
        } finally {
          it.recycle()
        }
      }
    }
  }

  /**
   * This method will be called if a View of type T was inflated and it had one of the attributes specified by
   * [AttrsDecorator.styleable] set.
   *
   * @param view The view object that is being decorated.
   * @param typedArray A [TypedArray] for attributes.
   */
  protected abstract fun apply(view: T, typedArray: TypedArray)

  /**
   * The class for the given view
   *
   * @return The class for T
   */
  protected abstract fun getType(): Class<T>

  /**
   * Attributes supported by this decorator.
   *
   * @return an array of android attr resource ids.
   */
  protected abstract fun styleable(): IntArray

  /**
   * The default style specified by `defStyleAttr`
   *
   * @return An attribute in the current theme that contains a reference to a style resource that supplies defaults
   * values for the TypedArray. Can be `0` to not look for defaults.
   */
  @Suppress("MemberVisibilityCanBePrivate")
  protected fun defStyleAttr(): Int = 0

  /**
   * The style resource specified in the AttributeSet (named "style").
   *
   * @return A resource identifier of a style resource that supplies default values for the TypedArray, used only if
   * defStyleAttr is `0` or can not be found in the theme.  Can be `0` to not look for defaults.
   */
  @Suppress("MemberVisibilityCanBePrivate")
  protected fun defStyleRes(): Int = 0

  /**
   * Get the attributes
   *
   * @param context the context held by the view
   * @param attributeSet A read-only set of tag attributes.
   * @return a TypedArray holding an array of the attribute values.
   */
  @Suppress("MemberVisibilityCanBePrivate")
  protected fun obtainAttributes(context: Context, attributeSet: AttributeSet): TypedArray? =
      context.theme.obtainStyledAttributes(attributeSet, styleable(), defStyleAttr(), defStyleRes())

}