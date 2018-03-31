package com.jaredrummler.cyanea.utils

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.v4.graphics.ColorUtils

class ColorUtils private constructor() {

  init {
    throw AssertionError("no instances")
  }

  companion object {

    /**
     * Darkens a color by a given factor.
     *
     * @param color The color to darken
     * @param factor The factor to darken the color.
     * @return darker version of specified color.
     */
    @JvmStatic
    @ColorInt fun darker(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
      return Color.argb(Color.alpha(color), Math.max((Color.red(color) * factor).toInt(), 0),
          Math.max((Color.green(color) * factor).toInt(), 0),
          Math.max((Color.blue(color) * factor).toInt(), 0))
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color The color to lighten
     * @param factor The factor to lighten the color. 0 will make the color unchanged. 1 will make the color white.
     * @return lighter version of the specified color.
     */
    @JvmStatic
    @ColorInt fun lighter(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
      val alpha = Color.alpha(color)
      val red = ((Color.red(color) * (1 - factor) / 255 + factor) * 255).toInt()
      val green = ((Color.green(color) * (1 - factor) / 255 + factor) * 255).toInt()
      val blue = ((Color.blue(color) * (1 - factor) / 255 + factor) * 255).toInt()
      return Color.argb(alpha, red, green, blue)
    }

    /**
     * Returns `true` if the luminance of the color is less than or equal to 0.5
     *
     * @param color The color to calculate the luminance.
     * @return `true` if the color is dark
     */
    @JvmStatic
    fun isDarkColor(@ColorInt color: Int): Boolean {
      return isDarkColor(color, 0.5)
    }

    /**
     * Returns `true` if the luminance of the color is less than or equal to the luminance factor
     *
     * @param color The color to calculate the luminance.
     * @param luminance Value from 0-1. 1 = white. 0 = black.
     * @return `true` if the color is dark
     */
    @JvmStatic
    fun isDarkColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) luminance: Double): Boolean {
      return ColorUtils.calculateLuminance(color) <= luminance
    }

    /**
     * Returns `true` if the luminance of the color is greater than or equal to 0.5
     *
     * @param color The color to calculate the luminance.
     * @return `true` if the color is light
     */
    @JvmStatic
    fun isLightColor(@ColorInt color: Int): Boolean {
      return isLightColor(color, 0.5)
    }

    /**
     * Returns `true` if the luminance of the color is less than or equal to the luminance factor
     *
     * @param color The color to calculate the luminance.
     * @param luminance Value from 0-1. 1 = white. 0 = black.
     * @return `true` if the color is light
     */
    @JvmStatic
    fun isLightColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) luminance: Double): Boolean {
      return ColorUtils.calculateLuminance(color) >= luminance
    }

    /**
     * Manipulate the alpha bytes of a color
     *
     * @param color The color to adjust the alpha on
     * @param factor 0.0f - 1.0f
     * @return The new color value
     */
    @JvmStatic
    @ColorInt fun adjustAlpha(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
      return (factor * 255.0f).toInt() shl 24 or (color and 0x00ffffff)
    }

  }

}