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

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.EdgeEffect
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.annotation.ColorInt
import androidx.core.widget.EdgeEffectCompat
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.ViewPager
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.utils.ColorFilterCompat
import com.jaredrummler.cyanea.utils.Reflection

/**
 * Provides utility methods to set the color of an [EdgeEffect].
 *
 * Example usage:
 *
 * ```kotlin
 * if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
 *     EdgeEffectTint(activity).tint(ContextCompat.getColor(activity, R.color.cyanea_primary))
 * }
 * ```
 */
class EdgeEffectTint(private val view: ViewGroup) {

  constructor(activity: Activity) : this(
    activity.findViewById<View>(android.R.id.content).rootView as ViewGroup
  )

  /**
   * Sets the color on all edge effects for the [ViewGroup] *and* its children passed to the constructor.
   *
   * @param color The color to be applied on the [EdgeEffect]
   */
  fun tint(@ColorInt color: Int) = setEdgeTint(view, color)

  private fun setEdgeTint(viewGroup: ViewGroup, @ColorInt color: Int) {
    for (i in 0 until viewGroup.childCount) {
      viewGroup.getChildAt(i)?.let { child ->
        if (!setEdgeGlowColor(child, color) && child is ViewGroup) {
          setEdgeTint(child, color)
        }
      }
    }
  }

  companion object {

    private const val TAG = "EdgeEffectTint"

    /**
     * Set the color of an [EdgeEffect]. This uses reflection on pre-L.
     *
     * @param edgeEffect The EdgeEffect to apply the color on.
     * @param color The color value
     */
    @JvmStatic
    fun setEdgeEffectColor(edgeEffect: EdgeEffect, @ColorInt color: Int) {
      try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          edgeEffect.color = color
          return
        }
        for (name in arrayOf("mEdge", "mGlow")) {
          Reflection.getFieldValue<Drawable?>(edgeEffect, name)?.run {
            colorFilter = ColorFilterCompat.SRC_IN.get(color)
            callback = null // free up any references
          }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting edge effect color", e)
      }
    }

    /**
     * Set the edge-effect glow color for a view.
     *
     * Supported views:
     *
     *  * [AbsListView]
     *  * [HorizontalScrollView]
     *  * [ScrollView]
     *  * [NestedScrollView]
     *  * [ViewPager]
     *  * [WebView]
     *
     * @param view The view to set the edge color
     * @param color The color value
     * @return `true` if the view was one of the supported views, `false` otherwise
     */
    fun setEdgeGlowColor(view: View, @ColorInt color: Int): Boolean {
      when (view) {
        is AbsListView -> setEdgeGlowColor(view, color)
        is HorizontalScrollView -> setEdgeGlowColor(view, color)
        is ScrollView -> setEdgeGlowColor(view, color)
        is NestedScrollView -> setEdgeGlowColor(view, color)
        is ViewPager -> setEdgeGlowColor(view, color)
        is WebView -> setEdgeGlowColor(view, color)
        else -> return false
      }
      return true
    }

    /**
     * Set the edge-effect color on a [NestedScrollView].
     *
     * @param scrollView The [NestedScrollView] to set the edge color on
     * @param color The color value
     */
    private fun setEdgeGlowColor(scrollView: NestedScrollView, @ColorInt color: Int) {
      try {
        Reflection.invoke<Any?>(scrollView, "ensureGlows")
        for (name in arrayOf("mEdgeGlowTop", "mEdgeGlowBottom")) {
          Reflection.getFieldValue<Any?>(scrollView, name)?.let { value ->
            if (value is EdgeEffect) {
              setEdgeEffectColor(value, color)
            } else if (value is EdgeEffectCompat) {
              Reflection.getFieldValue<EdgeEffect?>(value, "mEdgeEffect")?.let { edgeEffect ->
                setEdgeEffectColor(edgeEffect, color)
              }
            }
          }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting edge glow color on NestedScrollView", e)
      }
    }

    /**
     * Set the edge-effect color on a [ListView][android.widget.ListView] or [GridView][android.widget.GridView].
     *
     * @param listView The view (ListView/GridView) to set the edge color
     * @param color The color value
     */
    private fun setEdgeGlowColor(listView: AbsListView, @ColorInt color: Int) {
      try {
        for (name in arrayOf("mEdgeGlowTop", "mEdgeGlowBottom")) {
          Reflection.getFieldValue<EdgeEffect?>(listView, name)?.let { edgeEffect ->
            setEdgeEffectColor(edgeEffect, color)
          }
        }
      } catch (ignored: Exception) {
      }
    }

    /**
     * Set the edge-effect color on a [HorizontalScrollView].
     *
     * @param hsv The view to set the edge color
     * @param color The color value
     */
    private fun setEdgeGlowColor(hsv: HorizontalScrollView, @ColorInt color: Int) {
      try {
        for (name in arrayOf("mEdgeGlowLeft", "mEdgeGlowRight")) {
          val edgeEffect = Reflection.getFieldValue<EdgeEffect?>(hsv, name)
          edgeEffect?.let { setEdgeEffectColor(it, color) }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting edge glow color on HorizontalScrollView", e)
      }
    }

    /**
     * Set the edge-effect color on a [ScrollView].
     *
     * @param scrollView The view to set the edge color
     * @param color The color value
     */
    private fun setEdgeGlowColor(scrollView: ScrollView, color: Int) {
      try {
        for (name in arrayOf("mEdgeGlowTop", "mEdgeGlowBottom")) {
          val edgeEffect = Reflection.getFieldValue<EdgeEffect?>(scrollView, name)
          edgeEffect?.let { setEdgeEffectColor(it, color) }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting edge glow color on ScrollView", e)
      }
    }

    /**
     * Set the edge-effect color on a [ViewPager].
     *
     * @param viewPager the ViewPager
     * @param color The color value
     */
    private fun setEdgeGlowColor(viewPager: ViewPager, color: Int) {
      try {
        for (name in arrayOf("mLeftEdge", "mRightEdge")) {
          Reflection.getFieldValue<Any?>(viewPager, name)?.let { value ->
            if (value is EdgeEffect) {
              setEdgeEffectColor(value, color)
            } else if (value is EdgeEffectCompat) {
              Reflection.getFieldValue<EdgeEffect?>(value, "mEdgeEffect")?.let { edgeEffect ->
                setEdgeEffectColor(edgeEffect, color)
              }
            }
          }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting edge glow color on ViewPager", e)
      }
    }

    /**
     * Set the edge-effect color on a [WebView].
     *
     * @param webView
     * the WebView
     * @param color
     * the color value
     */
    private fun setEdgeGlowColor(webView: WebView, color: Int) {
      try {
        val provider = Reflection.invoke<Any?>(webView, "getWebViewProvider")
        val delegate = Reflection.invoke<Any?>(provider, "getViewDelegate")
        val mAwContents = Reflection.getFieldValue<Any?>(delegate, "mAwContents")
        val mOverScrollGlow = Reflection.getFieldValue<Any?>(mAwContents, "mOverScrollGlow")
        val names = arrayOf("mEdgeGlowTop", "mEdgeGlowBottom", "mEdgeGlowLeft", "mEdgeGlowRight")
        for (name in names) {
          Reflection.getFieldValue<EdgeEffect?>(mOverScrollGlow, name)?.let {
            setEdgeEffectColor(it, color)
          }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error setting edge glow color on WebView", e)
      }
    }
  }
}
