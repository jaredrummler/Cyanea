package com.jaredrummler.cyanea

import android.content.res.Resources
import android.os.Build

class CyaneaResources(private val cyanea: Cyanea, original: Resources) : Resources(
    original.getAssets(), original.getDisplayMetrics(), original.getConfiguration()
) {

  // TODO: color drawables and tint drawables on API 23+

  override fun getColor(id: Int): Int {
    return this.getColor(id, null)
  }

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

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) super.getColor(id) else super.getColor(id, theme)
  }

  // TODO: theme color state list

}