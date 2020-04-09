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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewConfiguration
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.cyanea.delegate.BaseAppCompatDelegate
import com.jaredrummler.cyanea.utils.Reflection
import java.lang.ref.WeakReference

/**
 * Tint the action bar, status bar, and/or the navigation bar.
 *
 * Usage:
 *
 * ```
 * val tint = SystemBarTint(activity)
 * tint.setActionBarColor(ContextCompat.getColor(activity, R.color.action_bar_color))
 * tint.setStatusBarColor(ContextCompat.getColor(activity, R.color.status_bar_color))
 * tint.setNavigationBarColor(ContextCompat.getColor(activity, R.color.navigation_bar_color))
 * ```
 */
@SuppressLint("ResourceType", "InlinedApi")
class SystemBarTint(activity: Activity) {

  private val activityRef: WeakReference<Activity>
  private var isStatusBarAvailable: Boolean = false
  private var isNavBarAvailable: Boolean = false
  private var oldActionBarBackground: Drawable? = null
  private var actionBar: Any? = null
  val sysBarConfig: SysBarConfig

  private val drawableCallback = object : Drawable.Callback {

    override fun invalidateDrawable(who: Drawable) {
      actionBar?.run {
        if (this is android.app.ActionBar) {
          setBackgroundDrawable(who)
        } else if (this is ActionBar) {
          setBackgroundDrawable(who)
        }
      }
    }

    override fun scheduleDrawable(drawable: Drawable, action: Runnable, time: Long) {
      Handler().postAtTime(action, time)
    }

    override fun unscheduleDrawable(drawable: Drawable, action: Runnable) {
      Handler().removeCallbacks(action)
    }
  }

  init {
    val win = activity.window
    activityRef = WeakReference(activity)

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
      // check theme attrs
      val attrs = intArrayOf(android.R.attr.windowTranslucentStatus, android.R.attr.windowTranslucentNavigation)
      val a = activity.obtainStyledAttributes(attrs)
      try {
        isStatusBarAvailable = a.getBoolean(0, false)
        isNavBarAvailable = a.getBoolean(1, false)
      } finally {
        a.recycle()
      }

      // check window flags
      val winParams = win.attributes
      if (winParams.flags and FLAG_TRANSLUCENT_STATUS != 0) {
        isStatusBarAvailable = true
      }
      if (winParams.flags and FLAG_TRANSLUCENT_NAVIGATION != 0) {
        isNavBarAvailable = true
      }
    } else {
      isStatusBarAvailable = false
      isNavBarAvailable = false
    }

    sysBarConfig = SysBarConfig(activity)

    // device might not have virtual navigation keys
    if (!sysBarConfig.hasNavigationBar) {
      isNavBarAvailable = false
    }

    actionBar = activity.actionBar ?: when (activity) {
      is AppCompatActivity -> activity.supportActionBar
      is BaseAppCompatDelegate -> activity.getDelegate().supportActionBar
      else -> null
    }
  }

  /**
   * Set the given color on the status bar.
   *
   * @param color The color value to be applies
   */
  fun setStatusBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val activity = activityRef.get() ?: return
      activity.window.statusBarColor = color
      return
    }
  }

  /**
   * Set the given color on the navigation bar.
   *
   * @param color the color to be applied.
   */
  fun setNavigationBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val activity = activityRef.get() ?: return
      activity.window.navigationBarColor = color
      return
    }
  }

  /**
   * Set the given color on the ActionBar/Toolbar.
   *
   * @param color The color value to be applied.
   */
  fun setActionBarColor(@ColorInt color: Int) {
    actionBar?.let { ab ->
      val colorDrawable = ColorDrawable(color)

      oldActionBarBackground?.let { oldBackground ->
        val td = TransitionDrawable(arrayOf(oldBackground, colorDrawable))
        // workaround for broken ActionBarContainer drawable handling on pre-API 17 builds
        // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
        when {
          Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 -> td.callback = drawableCallback
          ab is android.app.ActionBar -> ab.setBackgroundDrawable(td)
          ab is ActionBar -> ab.setBackgroundDrawable(td)
        }
        td.startTransition(200)
      } ?: run {
        when {
          Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
            val td = TransitionDrawable(arrayOf<Drawable>(colorDrawable, colorDrawable))
            td.callback = drawableCallback
            td.startTransition(200)
          }
          ab is android.app.ActionBar -> ab.setBackgroundDrawable(colorDrawable)
          ab is ActionBar -> ab.setBackgroundDrawable(colorDrawable)
        }
      }

      oldActionBarBackground = colorDrawable

      // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
      if (ab is android.app.ActionBar) {
        val isDisplayingTitle = ab.displayOptions and android.app.ActionBar.DISPLAY_SHOW_TITLE != 0
        ab.setDisplayShowTitleEnabled(!isDisplayingTitle)
        ab.setDisplayShowTitleEnabled(isDisplayingTitle)
      } else if (ab is ActionBar) {
        val isDisplayingTitle = ab.displayOptions and ActionBar.DISPLAY_SHOW_TITLE != 0
        ab.setDisplayShowTitleEnabled(!isDisplayingTitle)
        ab.setDisplayShowTitleEnabled(isDisplayingTitle)
      }
    }
  }

  companion object {
    /** The default system bar tint color value. 60% opacity, black  */
    private const val DEFAULT_TINT_COLOR = 0x99000000.toInt()
  }

  /**
   * Describes system bar sizing and other characteristics for the current device configuration.
   */
  class SysBarConfig(activity: Activity) {

    private val smallestWidthDp: Float
    private val inPortrait: Boolean

    /** The height of the status bar (in pixels). */
    val statusBarHeight: Int

    /** The height of the action bar (in pixels). */
    @Suppress("MemberVisibilityCanBePrivate")
    val actionBarHeight: Int

    /** The height of the system navigation bar. */
    val navigationBarHeight: Int

    /** The width of the system navigation bar when it is placed vertically on the screen. */
    val navigationBarWidth: Int

    /** True if this device uses soft key navigation, False otherwise. */
    val hasNavigationBar: Boolean

    /** True if navigation should appear at the bottom of the screen, False otherwise. */
    val isNavigationAtBottom: Boolean
      get() = smallestWidthDp >= 600 || inPortrait

    init {
      val res = activity.resources
      inPortrait = res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
      smallestWidthDp = getSmallestWidthDp(activity)
      statusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME)
      actionBarHeight = getActionBarHeight(activity)
      navigationBarHeight = getNavigationBarHeight(activity)
      navigationBarWidth = getNavigationBarWidth(activity)
      hasNavigationBar = navigationBarHeight > 0
    }

    private fun getSmallestWidthDp(activity: Activity): Float {
      val metrics = DisplayMetrics()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        // Display#getRealMetrics was hidden until API jb-mr0 but available since ics-mr0
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
      } else {
        // This is not correct, but we don't really care pre-kitkat
        activity.windowManager.defaultDisplay.getMetrics(metrics)
      }
      val widthDp = metrics.widthPixels / metrics.density
      val heightDp = metrics.heightPixels / metrics.density
      return Math.min(widthDp, heightDp)
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
      var result = 0
      val resourceId = res.getIdentifier(key, "dimen", "android")
      if (resourceId > 0) {
        result = res.getDimensionPixelSize(resourceId)
      }
      return result
    }

    private fun getActionBarHeight(context: Context): Int {
      var result = 0
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        val tv = TypedValue()
        context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
        result = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
      }
      return result
    }

    private fun getNavigationBarHeight(context: Context): Int {
      val res = context.resources
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        if (hasNavBar(context)) {
          val key = if (inPortrait) {
            NAV_BAR_HEIGHT_RES_NAME
          } else {
            NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME
          }
          return getInternalDimensionSize(res, key)
        }
      }
      return 0
    }

    private fun getNavigationBarWidth(context: Context): Int {
      val res = context.resources
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        if (hasNavBar(context)) {
          return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME)
        }
      }
      return 0
    }

    private fun hasNavBar(context: Context): Boolean {
      val res = context.resources
      val resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android")
      if (resourceId != 0) {
        var hasNav = res.getBoolean(resourceId)
        // check override flag
        when (NAV_BAR_OVERRIDE) {
          "1" -> hasNav = false
          "0" -> hasNav = true
        }
        return hasNav
      }
      return !ViewConfiguration.get(context).hasPermanentMenuKey()
    }

    @SuppressLint("PrivateApi")
    companion object {
      private const val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
      private const val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
      private const val NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape"
      private const val NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width"
      private const val SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar"

      @SuppressLint("PrivateApi")
      private val NAV_BAR_OVERRIDE = try {
        val systemPropertiesClass = Class.forName("android.os.SystemProperties")
        Reflection.invoke<String>(systemPropertiesClass, "get",
          arrayOf(String::class.java, String::class.java), "qemu.hw.mainkeys", "") ?: ""
      } catch (ignored: Exception) {
        ""
      }
    }
  }
}
