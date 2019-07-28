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

package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import com.jaredrummler.cyanea.Cyanea

@RequiresApi(Build.VERSION_CODES.KITKAT)
@TargetApi(Build.VERSION_CODES.KITKAT)
internal open class CyaneaDelegateImplV19(
  private val activity: Activity,
  private val cyanea: Cyanea,
  themeResId: Int
) : CyaneaDelegateImplBase(activity, cyanea, themeResId) {

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
