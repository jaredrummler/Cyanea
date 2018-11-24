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

package com.jaredrummler.cyanea.tinting

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.utils.Reflection

/**
 * Contains helper functions to tint aspects of various widgets
 */
class WidgetTint private constructor() {

  init {
    throw AssertionError("no instances")
  }

  companion object {

    private const val TAG = "WidgetTint"

    /**
     * Uses reflection to set the color on the fast scroll thumb in a ListView or GridView.
     *
     * @param listView The ListView or GridView.
     * @param color The color to set the fast scroll thumb.
     */
    fun setFastScrollThumbColor(listView: AbsListView, @ColorInt color: Int) {
      try {
        val name = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) "mFastScroll" else "mFastScroller"
        val scroller = Reflection.getFieldValue<Any?>(listView, name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          Reflection.getFieldValue<ImageView?>(scroller, "mThumbImage")
              ?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        } else {
          Reflection.getFieldValue<Drawable?>(scroller, "mThumbDrawable")
              ?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error tinting the fast scroll thumb", e)
      }
    }

    /**
     * Set the cursor color on an EditText.
     *
     * @param textView The view to tint, must extend or be a [TextView]
     * @param color The color value used to tint the cursor drawable.
     */
    fun setCursorColor(textView: TextView, @ColorInt color: Int) {
      try {
        Reflection.getField(TextView::class.java, "mCursorDrawableRes")?.let { fCursorDrawableRes ->
          Reflection.getField(TextView::class.java, "mEditor")?.let { fEditor ->
            fEditor.get(textView)?.let { editor ->
              Reflection.getField(editor, "mCursorDrawable")?.let { fCursorDrawable ->
                val cursorDrawableRes = fCursorDrawableRes.getInt(textView)
                ContextCompat.getDrawable(textView.context, cursorDrawableRes)?.let { drawable ->
                  drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                  val drawables = arrayOf(drawable, drawable)
                  fCursorDrawable.set(editor, drawables)
                }
              }
            }
          }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting cursor color", e)
      }
    }

  }

}