package com.jaredrummler.cyanea

import androidx.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea.BaseTheme.DARK
import com.jaredrummler.cyanea.Cyanea.BaseTheme.LIGHT

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
    get() {
      return when (cyanea.baseTheme) {
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
    }

  /**
   * Get a NoActionBar theme
   *
   * @return One of the following themes:
   * [R.style.Theme_Cyanea_Dark_NoActionBar],
   * [R.style.Theme_Cyanea_Light_NoActionBar]
   */
  @get:StyleRes
  val noActionBarTheme: Int
    get() {
      return when (cyanea.baseTheme) {
        DARK -> R.style.Theme_Cyanea_Dark_NoActionBar
        LIGHT -> R.style.Theme_Cyanea_Light_NoActionBar
      }
    }

}