package com.jaredrummler.cyanea

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.tinting.CyaneaTinter.CyaneaTintException
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * Resources to get custom colors from [Cyanea]
 */
@Suppress("DEPRECATION", "OverridingDeprecatedMember")
class CyaneaResources(original: Resources, private val cyanea: Cyanea = Cyanea.instance)
  : Resources(original.assets, original.displayMetrics, original.configuration) {

  init {
    cyanea.tinter.setup(original, this)
  }

  /* Track resources so we don't attempt to modify the Drawable or ColorStateList more than once */
  private val tintTracker = TintTracker()

  @Throws(Resources.NotFoundException::class)
  override fun getDrawable(id: Int): Drawable {
    return this.getDrawable(id, null)
  }

  @SuppressLint("PrivateResource")
  @Throws(Resources.NotFoundException::class)
  override fun getDrawable(id: Int, theme: Theme?): Drawable {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      super.getDrawable(id, theme).let { drawable ->
        if (!tintTracker.contains(id, theme)) {
          try {
            cyanea.tinter.tint(drawable)
          } catch (e: CyaneaTintException) {
            Cyanea.log(TAG, "Error tinting drawable", e)
          }
          tintTracker.add(id, theme)
        }
        return drawable
      }
    }
    return when (id) {
      R.color.background_material_dark, R.drawable.color_background_dark
      -> ColorDrawable(cyanea.backgroundDark)
      R.color.background_material_dark_darker, R.drawable.color_background_dark_darker
      -> ColorDrawable(cyanea.backgroundDarkDarker)
      R.color.background_material_dark_lighter, R.drawable.color_background_dark_lighter
      -> ColorDrawable(cyanea.backgroundDarkLighter)
      R.color.background_material_light, R.drawable.color_background_light
      -> ColorDrawable(cyanea.backgroundLight)
      R.color.background_material_light_darker, R.drawable.color_background_light_darker
      -> ColorDrawable(cyanea.backgroundLightDarker)
      R.color.background_material_light_lighter, R.drawable.color_background_light_lighter
      -> ColorDrawable(cyanea.backgroundLightLighter)
      else -> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
          super.getDrawable(id) else super.getDrawable(id, theme)
      }
    }
  }

  @Throws(Resources.NotFoundException::class)
  override fun getColor(id: Int): Int {
    return this.getColor(id, null)
  }

  @SuppressLint("PrivateResource")
  @Throws(Resources.NotFoundException::class)
  override fun getColor(id: Int, theme: Theme?): Int = when (id) {
    // ------ PRIMARY COLORS ------
    R.color.color_primary_reference, R.color.color_primary -> cyanea.primary
    R.color.color_primary_dark_reference, R.color.color_primary_dark -> cyanea.primaryDark
    R.color.color_primary_light_reference, R.color.color_primary_light -> cyanea.primaryLight
    // ------ ACCENT COLORS ------
    R.color.color_accent_reference, R.color.color_accent -> cyanea.accent
    R.color.color_accent_light_reference, R.color.color_accent_light -> cyanea.accentLight
    R.color.color_accent_dark_reference, R.color.color_accent_dark -> cyanea.accentDark
    // ------ BACKGROUND COLORS ------
    R.color.color_background_dark, R.color.background_material_dark -> cyanea.backgroundDark
    R.color.background_material_dark_lighter -> cyanea.backgroundDarkLighter
    R.color.background_material_dark_darker -> cyanea.backgroundDarkDarker
    R.color.color_background_light, R.color.background_material_light -> cyanea.backgroundLight
    R.color.background_material_light_darker -> cyanea.backgroundLightDarker
    R.color.background_material_light_lighter -> cyanea.backgroundLightLighter
    else -> {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        super.getColor(id) else super.getColor(id, theme)
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  @Throws(Resources.NotFoundException::class)
  override fun getColorStateList(id: Int): ColorStateList? {
    return super.getColorStateList(id)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  @Throws(Resources.NotFoundException::class)
  override fun getColorStateList(id: Int, theme: Resources.Theme?): ColorStateList? {
    val colorStateList = super.getColorStateList(id, theme)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!tintTracker.contains(id, theme)) {
        cyanea.tinter.tint(colorStateList)
        tintTracker.add(id, theme)
      }
    }
    return colorStateList
  }

  private inner class TintTracker {

    private val cache: MutableSet<Int> by lazy {
      Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())
    }

    internal fun contains(id: Int, theme: Resources.Theme?): Boolean = cache.contains(key(id, theme))

    internal fun add(id: Int, theme: Resources.Theme?): Boolean = cache.add(key(id, theme))

    private fun key(id: Int, theme: Resources.Theme?): Int = id + (theme?.hashCode() ?: 0)

  }

  companion object {
    private const val TAG = "CyaneaResources"

  }

}