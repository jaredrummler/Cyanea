package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import com.jaredrummler.cyanea.Cyanea

@RequiresApi(Build.VERSION_CODES.KITKAT)
@TargetApi(Build.VERSION_CODES.KITKAT)
open class CyaneaDelegateImplV19(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplBase(activity, cyanea, themeResId) {

  override fun onPostCreate(savedInstanceState: Bundle?) {
    setupSystemBarTinting()
    super.onPostCreate(savedInstanceState)
  }

  private fun setupSystemBarTinting() {
    if (cyanea.isThemeModified && Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
      val content = activity.findViewById<View>(android.R.id.content)
      if (content is ViewGroup) {
        if (content.childCount == 1) {
          val child = content.getChildAt(0)
          if (child !is DrawerLayout) {
            child.fitsSystemWindows = true
            val id = activity.resources.getIdentifier("config_enableTranslucentDecor", "bool", "android")
            if (id != 0) {
              val enabled = activity.resources.getBoolean(id)
              if (enabled) {
                activity.window.setFlags(FLAG_TRANSLUCENT_STATUS, FLAG_TRANSLUCENT_STATUS)
                if (cyanea.shouldTintNavBar) {
                  activity.window.setFlags(FLAG_TRANSLUCENT_NAVIGATION, FLAG_TRANSLUCENT_NAVIGATION)
                }
              }
            }
          }
        }
      }
    }
  }

}