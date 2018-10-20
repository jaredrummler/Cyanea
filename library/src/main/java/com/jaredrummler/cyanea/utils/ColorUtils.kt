package com.jaredrummler.cyanea.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils

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
    @JvmOverloads
    @ColorInt
    fun darker(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) factor: Float = 0.85f): Int {
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
    @JvmOverloads
    @ColorInt
    fun lighter(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) factor: Float = 0.15f): Int {
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
    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
      return (factor * 255.0f).toInt() shl 24 or (color and 0x00ffffff)
    }

    @JvmStatic
    @JvmOverloads
    fun toHex(@ColorInt color: Int, alpha: Boolean = true): String {
      return if (alpha) {
        String.format("%08X", (color))
      } else {
        String.format("%06X", 0xFFFFFF and color)
      }
    }

    /**
     * Parse the color string, and return the corresponding color-int.
     * If the string cannot be parsed, throws an IllegalArgumentException
     * exception.
     */
    @JvmStatic
    @ColorInt
    fun parseColor(colorString: String): Int {
      try {
        if (colorString.startsWith("#")) {
          return parseColor(colorString.substring(1))
        }

        val length = colorString.length
        val a: Int
        var r: Int
        val g: Int
        var b = 0

        when {
          length == 0 -> {
            r = 0
            a = 255
            g = 0
          }
          length <= 2 -> {
            a = 255
            r = 0
            b = Integer.parseInt(colorString, 16)
            g = 0
          }
          length == 3 -> {
            a = 255
            r = Integer.parseInt(colorString.substring(0, 1), 16)
            g = Integer.parseInt(colorString.substring(1, 2), 16)
            b = Integer.parseInt(colorString.substring(2, 3), 16)
          }
          length == 4 -> {
            a = 255
            r = Integer.parseInt(colorString.substring(0, 2), 16)
            g = r
            r = 0
            b = Integer.parseInt(colorString.substring(2, 4), 16)
          }
          length == 5 -> {
            a = 255
            r = Integer.parseInt(colorString.substring(0, 1), 16)
            g = Integer.parseInt(colorString.substring(1, 3), 16)
            b = Integer.parseInt(colorString.substring(3, 5), 16)
          }
          length == 6 -> {
            a = 255
            r = Integer.parseInt(colorString.substring(0, 2), 16)
            g = Integer.parseInt(colorString.substring(2, 4), 16)
            b = Integer.parseInt(colorString.substring(4, 6), 16)
          }
          length == 7 -> {
            a = Integer.parseInt(colorString.substring(0, 1), 16)
            r = Integer.parseInt(colorString.substring(1, 3), 16)
            g = Integer.parseInt(colorString.substring(3, 5), 16)
            b = Integer.parseInt(colorString.substring(5, 7), 16)
          }
          length == 8 -> {
            a = Integer.parseInt(colorString.substring(0, 2), 16)
            r = Integer.parseInt(colorString.substring(2, 4), 16)
            g = Integer.parseInt(colorString.substring(4, 6), 16)
            b = Integer.parseInt(colorString.substring(6, 8), 16)
          }
          else -> {
            b = -1
            g = -1
            r = -1
            a = -1
          }
        }
        return Color.argb(a, r, g, b)
      } catch (e: NumberFormatException) {
        return Color.parseColor(colorString)
      }
    }

  }

}