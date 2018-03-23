package com.jaredrummler.cyanea

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build

class CyaneaResources(private val cyanea: Cyanea, original: Resources) : Resources(
    original.assets, original.displayMetrics, original.configuration
) {

  // TODO: tint drawables on API 23+

  override fun getDrawable(id: Int): Drawable {
    return this.getDrawable(id, null)
  }

  @SuppressLint("PrivateResource")
  override fun getDrawable(id: Int, theme: Theme?): Drawable {

    when (id) {
      R.color.background_material_dark, R.drawable.color_background_dark
      -> return ColorDrawable(cyanea.backgroundDark)
      R.color.background_material_dark_darker, R.drawable.color_background_dark_darker
      -> return ColorDrawable(cyanea.backgroundDarkDarker)
      R.color.background_material_dark_lighter, R.drawable.color_background_dark_lighter
      -> return ColorDrawable(cyanea.backgroundDarkLighter)
      R.color.background_material_light, R.drawable.color_background_light
      -> return ColorDrawable(cyanea.backgroundLight)
      R.color.background_material_light_darker, R.drawable.color_background_light_darker
      -> return ColorDrawable(cyanea.backgroundLightDarker)
      R.color.background_material_light_lighter, R.drawable.color_background_light_lighter
      -> return ColorDrawable(cyanea.backgroundLightLighter)
    }

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
      super.getDrawable(id) else super.getDrawable(id, theme)
  }


  override fun getColor(id: Int): Int {
    return this.getColor(id, null)
  }

  @SuppressLint("PrivateResource")
  override fun getColor(id: Int, theme: Theme?): Int {

    when (id) {
    // ------ PRIMARY COLORS ------
      R.color.color_primary_reference, R.color.color_primary -> return cyanea.primary
      R.color.color_primary_dark_reference, R.color.color_primary_dark -> return cyanea.primaryDark
      R.color.color_primary_light_reference, R.color.color_primary_light -> return cyanea.primaryLight

    // ------ ACCENT COLORS ------
      R.color.color_accent_reference, R.color.color_accent -> return cyanea.accent
      R.color.color_accent_light_reference, R.color.color_accent_light -> return cyanea.accentLight
      R.color.color_accent_dark_reference, R.color.color_accent_dark -> return cyanea.accentDark

    // ------ BACKGROUND COLORS ------
      R.color.color_background_dark, R.color.background_material_dark -> return cyanea.backgroundDark
      R.color.background_material_dark_lighter -> return cyanea.backgroundDarkLighter
      R.color.background_material_dark_darker -> return cyanea.backgroundDarkDarker
      R.color.color_background_light, R.color.background_material_light -> return cyanea.backgroundLight
      R.color.background_material_light_darker -> return cyanea.backgroundLightDarker
      R.color.background_material_light_lighter -> return cyanea.backgroundLightLighter
    }

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
      super.getColor(id) else super.getColor(id, theme)
  }

  // TODO: theme color state list

}