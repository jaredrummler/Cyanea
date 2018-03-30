package com.jaredrummler.cyanea.tinting

import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaResources
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.utils.Reflection
import com.jaredrummler.cyanea.utils.Reflection.Companion.getFieldValue

/**
 * Apply color scheme to [drawables][Drawable] and [colors][ColorStateList]
 *
 * @param original The original resources. i.e. not the [CyaneaResources]
 * @param resources The [CyaneaResources] used to tint [drawables][Drawable] and [colors][ColorStateList]
 */
class CyaneaTinter(original: Resources, resources: CyaneaResources) {

  private val colors = HashMap<Int, Int>()

  init {
    COLOR_IDS.forEachIndexed { _, id ->
      @Suppress("DEPRECATION", "ReplacePutWithAssignment")
      colors.put(original.getColor(id), resources.getColor(id))
    }
  }

  /**
   * Tints the [Drawable.ConstantState] to match the colors from the [resources][CyaneaResources]
   *
   * @param drawable The [drawable][Drawable] to modify.
   */
  @Throws(CyaneaTintException::class)
  fun tint(drawable: Drawable) {
    if (drawable is GradientDrawable) {
      tint(drawable)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable is RippleDrawable) {
      tint(drawable)
    } else if (drawable is LayerDrawable) {
      tint(drawable)
    } else if (drawable is DrawableContainer) {
      tint(drawable)
    } else if (drawable is NinePatchDrawable) {
      tint(drawable)
    } else if (drawable is ColorDrawable) {
      tint(drawable)
    }
  }

  /**
   * Updates the colors in a [ColorStateList] to match the colors from the [resources][CyaneaResources]
   *
   * @param colorStateList The [color][ColorStateList] to modify
   * @return The modified [ColorStateList]
   */
  fun tint(colorStateList: ColorStateList?): ColorStateList? {
    return colorStateList?.let { colorStateList ->
      try {
        Reflection.invoke<IntArray?>(colorStateList, "getColors")?.let { colors ->
          var changed = false
          for (i in colors.indices) {
            this.colors[colors[i]]?.let { color ->
              if (color != colors[i]) {
                colors[i] = color
                changed = true
              }
            }
          }
          if (changed && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Reflection.invoke<Any?>(colorStateList, "onColorsChanged")
          }
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error tinting ColorStateList", e)
      }
      colorStateList
    }
  }

  @Throws(CyaneaTintException::class)
  private fun tint(drawable: GradientDrawable) {
    try {
      getFieldValue<Any?>(drawable, "mGradientState")?.let { state ->
        getFieldValue<ColorStateList?>(state, "mSolidColors")?.let { solidColors ->
          tint(solidColors)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setColor(solidColors)
          }
        }
      }
    } catch (e: Exception) {
      throw CyaneaTintException("Error tinting GradientDrawable", e)
    }
  }

  @Throws(CyaneaTintException::class)
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private fun tint(drawable: RippleDrawable) {
    try {
      getFieldValue<Any?>(drawable, "mState")?.let { state ->
        getFieldValue<ColorStateList?>(state, "mColor")?.let { color ->
          tint(color)
          Reflection.getField(state.javaClass.superclass, "mChildren")?.let { fChildren ->
            (fChildren.get(state) as? Array<Any?>)?.forEach {
              getFieldValue<Drawable?>(it, "mDrawable")?.let { drawable -> tint(drawable) }
            }
          }
        }
      }
    } catch (e: Exception) {
      throw CyaneaTintException("Error tinting RippleDrawable", e)
    }
  }

  @Throws(CyaneaTintException::class)
  private fun tint(drawable: LayerDrawable) {
    try {
      getFieldValue<Any?>(drawable, "mLayerState")?.let { state ->
        getFieldValue<Array<Any?>>(state, "mChildren")?.let { children ->
          children.forEach { child ->
            getFieldValue<Drawable?>(child, "mDrawable")?.let { drawable ->
              tint(drawable)
            }
          }
        }
      }
    } catch (e: Exception) {
      throw CyaneaTintException("Error tinting LayerDrawable", e)
    }
  }

  @Throws(CyaneaTintException::class)
  private fun tint(drawable: DrawableContainer) {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getFieldValue<DrawableContainerState?>(drawable, "mDrawableContainerState")?.let { state ->
          for (i in 0 until state.childCount) {
            tint(state.getChild(i))
          }
        }
      }
    } catch (e: Exception) {
      throw CyaneaTintException("Error tinting DrawableContainer", e)
    }
  }

  @Throws(CyaneaTintException::class)
  private fun tint(drawable: NinePatchDrawable) {
    try {
      Reflection.getFieldValue<Any?>(drawable, "mNinePatchState")?.let { ninePatchState ->
        Reflection.getFieldValue<ColorStateList?>(ninePatchState, "mTint")?.let { colorStateList ->
          tint(colorStateList)
        }
      }
    } catch (e: Exception) {
      throw CyaneaTintException("Error tinting NinePatchDrawable", e)
    }
  }

  private fun tint(drawable: ColorDrawable) {
    drawable.color = this.colors[drawable.color] ?: drawable.color
  }

  class CyaneaTintException(msg: String, e: Exception) : Exception(msg, e)

  companion object {

    private val TAG = "CyaneaTinter"

    private val COLOR_IDS = intArrayOf(
        R.color.background_material_dark,
        R.color.background_material_dark_darker,
        R.color.background_material_dark_lighter,
        R.color.background_material_light,
        R.color.background_material_light_darker,
        R.color.background_material_light_lighter,
        R.color.color_accent,
        R.color.color_accent_dark,
        R.color.color_accent_dark_reference,
        R.color.color_accent_light,
        R.color.color_accent_light_reference,
        R.color.color_accent_reference,
        R.color.color_background_light,
        R.color.color_primary,
        R.color.color_primary_dark,
        R.color.color_primary_dark_reference,
        R.color.color_primary_light,
        R.color.color_primary_light_reference,
        R.color.color_primary_reference,
        R.color.color_background_dark)

  }

}