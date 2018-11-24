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

import android.content.res.AssetManager
import android.content.res.TypedArray
import android.graphics.Typeface
import android.widget.TextView
import com.jaredrummler.cyanea.R

/**
 * Set a font from assets by adding an attribute to the view in your layout XML.
 *
 * ```xml
 * <TextView
 *     ...
 *     app:cyaneaFont="fonts/CustomFont.ttf"
 *     tools:ignore="MissingPrefix" />
 * ```
 */
class FontDecorator : AttrsDecorator<TextView>() {

  private val cache = mutableMapOf<String, Typeface>()

  override fun getType(): Class<TextView> = TextView::class.java

  override fun styleable(): IntArray = R.styleable.FontDecorator

  override fun apply(view: TextView, typedArray: TypedArray) {
    typedArray.getString(R.styleable.FontDecorator_cyaneaFont)?.let { path ->
      view.typeface = getFont(view.context.assets, path)
    }
  }

  private fun getFont(assets: AssetManager, path: String): Typeface? {
    return cache[path] ?: run {
      try {
        Typeface.createFromAsset(assets, path)?.let { font ->
          cache[path] = font
          font
        }
      } catch (e: Exception) {
        null
      }
    }
  }

}