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

package com.jaredrummler.cyanea

import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.cyanea.Cyanea.BaseTheme.DARK
import com.jaredrummler.cyanea.Cyanea.BaseTheme.LIGHT

/**
 * Theme that can be used in your Cyanea based activity.
 *
 * If using your own Toolbar via [AppCompatActivity.setSupportActionBar] use the following theme:
 *
 * ```kotlin
 * override fun getThemeResId(): Int = cyanea.themes.actionBarTheme
 * ```
 *
 * If using the default ActionBar use the following theme:
 *
 * ```kotlin
 * override fun getThemeResId(): Int = cyanea.themes.noActionBarTheme
 * ```
 */
class CyaneaThemes internal constructor(private val cyanea: Cyanea) {

  /**
   * Get a ActionBar theme.
   *
   * @return One of the following themes:
   * [R.style.Theme_Cyanea_Dark],
   * [R.style.Theme_Cyanea_Dark_LightActionBar],
   * [R.style.Theme_Cyanea_Light],
   * [R.style.Theme_Cyanea_Light_DarkActionBar]
   */
  @get:StyleRes
  val actionBarTheme: Int
    get() = when (cyanea.baseTheme) {
      DARK ->
        if (cyanea.isActionBarLight)
          R.style.Theme_Cyanea_Dark_LightActionBar
        else
          R.style.Theme_Cyanea_Dark
      LIGHT ->
        if (cyanea.isActionBarDark)
          R.style.Theme_Cyanea_Light_DarkActionBar
        else
          R.style.Theme_Cyanea_Light
    }

  /**
   * Get a NoActionBar theme
   *
   * @return One of the following themes:
   * [R.style.Theme_Cyanea_Dark_NoActionBar],
   * [R.style.Theme_Cyanea_Light_NoActionBar],
   * [R.style.Theme_Cyanea_Light_DarkActionBar_NoActionBar],
   * [R.style.Theme_Cyanea_Dark_LightActionBar_NoActionBar]
   */
  @get:StyleRes
  val noActionBarTheme: Int
    get() = when (cyanea.baseTheme) {
      DARK ->
        if (cyanea.isActionBarLight) // Check primary color for correct actionBarTheme
          R.style.Theme_Cyanea_Dark_LightActionBar_NoActionBar
        else
          R.style.Theme_Cyanea_Dark_NoActionBar
      LIGHT ->
        if (cyanea.isActionBarDark) // Check primary color for correct actionBarTheme
          R.style.Theme_Cyanea_Light_DarkActionBar_NoActionBar
        else
          R.style.Theme_Cyanea_Light_NoActionBar
    }

}