package com.jaredrummler.cyanea.tinting

import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaResources
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.utils.ColorUtils
import com.jaredrummler.cyanea.utils.Reflection
import com.jaredrummler.cyanea.utils.Reflection.Companion.getFieldValue
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Apply color scheme to [drawables][Drawable] and [colors][ColorStateList]
 */
class CyaneaTinter {

  private val colors = HashMap<Int, Int>()

  /**
   * Tints the [Drawable.ConstantState] to match the colors from the [resources][CyaneaResources]
   *
   * @param drawable The [drawable][Drawable] to modify.
   */
  @Throws(CyaneaTintException::class)
  fun tint(drawable: Drawable?) {
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
    return colorStateList?.let { csl ->
      fun updateColors(colors: IntArray): Boolean {
        var changed = false
        for (i in colors.indices) {
          this.colors[colors[i]]?.let { color ->
            if (color != colors[i]) {
              colors[i] = color
              changed = true
            }
          } ?: run {
            val stripAlpha = ColorUtils.stripAlpha(colors[i])
            this.colors[stripAlpha]?.run {
              val color = Color.argb(Color.alpha(colors[i]), Color.red(this), Color.green(this), Color.blue(this))
              colors[i] = color
              changed = true
            }
          }
        }
        return changed
      }

      try {
        var changed = false
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
          Reflection.getFieldValue<IntArray?>(csl, "mColors")?.let { colors ->
            changed = updateColors(colors)
          }
        } else {
          Reflection.invoke<IntArray?>(csl, "getColors")?.let { colors ->
            changed = updateColors(colors)
          }
        }
        if (changed && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          Reflection.invoke<Any?>(csl, "onColorsChanged")
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error tinting ColorStateList", e)
      }
      csl
    }
  }

  @JvmOverloads
  fun tint(view: View, recursive: Boolean = true) {
    try {
      var klass: Class<*>? = view.javaClass
      do {
        klass?.declaredFields?.forEach { field ->
          if (Modifier.isStatic(field.modifiers)) return@forEach
          if (field.type == ColorStateList::class.java) {
            get<ColorStateList>(field, view)?.let { csl ->
              tint(csl)
            }
          } else if (field.type == Drawable::class.java) {
            get<Drawable>(field, view)?.let { drawable ->
              tint(drawable)
            }
          }
        }
        klass = klass?.superclass
      } while (klass != null)
    } catch (e: Exception) {
      Cyanea.log(TAG, "Error tinting view: $view", e)
    }
    if (recursive && view is ViewGroup) {
      for (i in 0 until view.childCount) {
        view.getChildAt(i)?.let { v ->
          tint(v, recursive)
        }
      }
    }
  }

  /**
   * Setup the colors for tinting drawables and color state lists on API 23+
   *
   * @param original The original resources. i.e. not the [CyaneaResources]
   * @param resources The [CyaneaResources] used to tint [drawables][Drawable] and [colors][ColorStateList]
   */
  @Suppress("DEPRECATION")
  internal fun setup(original: Resources, resources: CyaneaResources) {
    COLOR_IDS.forEach { id ->
      colors[original.getColor(id)] = resources.getColor(id)
    }
  }

  private inline fun <reified T> get(field: Field, obj: Any): T? {
    if (!field.isAccessible) {
      field.isAccessible = true
    }
    if (Modifier.isFinal(field.modifiers)) {
      val modifiersField = Field::class.java.getDeclaredField("modifiers")
      modifiersField.isAccessible = true
      modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    }
    return field.get(obj) as? T
  }

  @Throws(CyaneaTintException::class)
  private fun tint(drawable: GradientDrawable) {
    try {
      getFieldValue<Any?>(drawable, "mGradientState")?.let { state ->
        getFieldValue<ColorStateList?>(state, "mSolidColors")?.let { solidColors ->
          tint(solidColors)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.color = solidColors
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
            (fChildren.get(state) as? Array<*>)?.forEach {
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      // Android API 28 blocks getting the field NinePatchState#mTint =(
      return
    }
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

    private const val TAG = "CyaneaTinter"

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