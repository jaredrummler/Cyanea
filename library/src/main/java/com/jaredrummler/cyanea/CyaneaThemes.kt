package com.jaredrummler.cyanea

import androidx.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea.BaseTheme.DARK
import com.jaredrummler.cyanea.Cyanea.BaseTheme.LIGHT

class CyaneaThemes internal constructor(private val cyanea: Cyanea) {

  /**
   * Get a ActionBar theme.
   *
   * @return One of the following themes:
   * [R.style.Cyanea_Dark],
   * [R.style.Cyanea_Dark_LightActionBar],
   * [R.style.Cyanea_Light],
   * [R.style.Cyanea_Light_DarkActionBar]
   */
  @get:StyleRes
  val actionBarTheme: Int
    get() {
      return when (cyanea.baseTheme) {
        DARK ->
          if (cyanea.isActionBarLight)
            R.style.Cyanea_Dark_LightActionBar
          else
            R.style.Cyanea_Dark
        LIGHT ->
          if (cyanea.isActionBarDark)
            R.style.Cyanea_Light_DarkActionBar
          else
            R.style.Cyanea_Light
      }
    }

  /**
   * Get a NoActionBar theme
   *
   * @return One of the following themes:
   * [R.style.Cyanea_Dark_NoActionBar],
   * [R.style.Cyanea_Dark_LightActionBar_NoActionBar],
   * [R.style.Cyanea_Light_NoActionBar],
   * [R.style.Cyanea_Dark_LightActionBar]
   */
  @get:StyleRes
  val noActionBarTheme: Int
    get() {
      return when (cyanea.baseTheme) {
        DARK ->
          if (cyanea.isActionBarLight)
            R.style.Cyanea_Dark_LightActionBar_NoActionBar
          else
            R.style.Cyanea_Dark_NoActionBar
        LIGHT ->
          if (cyanea.isActionBarDark)
            R.style.Cyanea_Light_DarkActionBar_NoActionBar
          else
            R.style.Cyanea_Light_NoActionBar
      }
    }

  /**
   * Get a NoActionBar_Overlay theme
   *
   * @return One of the following themes:
   * [R.style.Cyanea_Dark_NoActionBar_Overlay],
   * [R.style.Cyanea_Dark_LightActionBar_NoActionBar_Overlay],
   * [R.style.Cyanea_Light_NoActionBar_Overlay],
   * [R.style.Cyanea_Dark_LightActionBar]
   */
  @get:StyleRes
  val noActionBarOverlayTheme: Int
    get() {
      return when (cyanea.baseTheme) {
        DARK ->
          if (cyanea.isActionBarLight)
            R.style.Cyanea_Dark_LightActionBar_NoActionBar_Overlay
          else
            R.style.Cyanea_Dark_NoActionBar_Overlay
        LIGHT ->
          if (cyanea.isActionBarDark)
            R.style.Cyanea_Light_DarkActionBar_NoActionBar_Overlay
          else
            R.style.Cyanea_Light_NoActionBar_Overlay
      }
    }

  /**
   * Get an translucent theme
   *
   * @return One of the following themes:
   * [R.style.Cyanea_Dark_NoActionBar_Translucent],
   * [R.style.Cyanea_Dark_LightActionBar_NoActionBar_Translucent],
   * [R.style.Cyanea_Light_DarkActionBar_NoActionBar_Translucent],
   * [R.style.Cyanea_Light_NoActionBar_Translucent]
   */
  @get:StyleRes
  val noActionBarTranslucentTheme: Int
    get() {
      return when (cyanea.baseTheme) {
        DARK ->
          if (cyanea.isActionBarLight)
            R.style.Cyanea_Dark_LightActionBar_NoActionBar_Translucent
          else
            R.style.Cyanea_Dark_NoActionBar_Translucent
        LIGHT ->
          if (cyanea.isActionBarDark)
            R.style.Cyanea_Light_DarkActionBar_NoActionBar_Translucent
          else
            R.style.Cyanea_Light_NoActionBar_Translucent
      }
    }

  /**
   * Get an overlay theme
   *
   * @return One of the following themes:
   * [R.style.Cyanea_Dark_Overlay],
   * [R.style.Cyanea_Dark_LightActionBar_Overlay],
   * [R.style.Cyanea_Light_DarkActionBar_Overlay],
   * [R.style.Cyanea_Light_Overlay]
   */
  @get:StyleRes
  val overlayTheme: Int
    get() {
      return when (cyanea.baseTheme) {
        Cyanea.BaseTheme.DARK ->
          if (cyanea.isActionBarLight)
            R.style.Cyanea_Dark_LightActionBar_Overlay
          else
            R.style.Cyanea_Dark_Overlay
        Cyanea.BaseTheme.LIGHT ->
          if (cyanea.isActionBarDark)
            R.style.Cyanea_Light_DarkActionBar_Overlay
          else
            R.style.Cyanea_Light_Overlay
      }
    }

}