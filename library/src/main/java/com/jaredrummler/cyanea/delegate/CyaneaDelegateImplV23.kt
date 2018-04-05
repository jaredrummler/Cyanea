package com.jaredrummler.cyanea.delegate

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.inflator.processors.AlertDialogProcessor
import com.jaredrummler.cyanea.inflator.processors.CompoundButtonProcessor
import com.jaredrummler.cyanea.inflator.processors.CyaneaViewProcessor
import com.jaredrummler.cyanea.inflator.processors.ImageButtonProcessor
import com.jaredrummler.cyanea.inflator.processors.ListMenuItemViewProcessor
import com.jaredrummler.cyanea.inflator.processors.SearchAutoCompleteProcessor
import com.jaredrummler.cyanea.inflator.processors.SwitchProcessor
import com.jaredrummler.cyanea.inflator.processors.TextViewProcessor
import com.jaredrummler.cyanea.inflator.processors.ViewGroupProcessor

@RequiresApi(Build.VERSION_CODES.M)
@TargetApi(Build.VERSION_CODES.M)
open class CyaneaDelegateImplV23(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplV21(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      PRELOADED_COLORS.forEach {
        // Load and update the colors before views are inflated
        activity.resources.getColorStateList(it, activity.theme)
      }

      PRELOADED_DRAWABLES.forEach {
        // Update the drawable's ConstantState before views are inflated.
        activity.resources.getDrawable(it, activity.theme)
      }
    }
  }

  override fun getProcessorsForTheming(): List<CyaneaViewProcessor<*>> {
    val processors = mutableListOf<CyaneaViewProcessor<*>>()
    processors.addAll(super.getProcessorsForTheming())
    processors.add(ListMenuItemViewProcessor())
    processors.add(AlertDialogProcessor())
    processors.add(TextViewProcessor())
    processors.add(SearchAutoCompleteProcessor())
    processors.add(SwitchProcessor())
    processors.add(CompoundButtonProcessor())
    processors.add(ImageButtonProcessor())
    processors.add(ViewGroupProcessor())
    return processors
  }

  companion object {

    @SuppressLint("PrivateResource")
    private val PRELOADED_COLORS = intArrayOf(
        R.color.color_primary,
        R.color.color_primary_light,
        R.color.color_primary_dark,
        R.color.color_accent,
        R.color.color_accent_light,
        R.color.color_accent_dark,
        R.color.color_background_dark,
        R.color.color_background_dark_lighter,
        R.color.color_background_dark_darker,
        R.color.color_background_light,
        R.color.color_background_light_lighter,
        R.color.color_background_light_darker,
        R.color.background_material_dark,
        R.color.background_material_dark_lighter,
        R.color.background_material_dark_darker,
        R.color.background_material_light,
        R.color.background_material_light_lighter,
        R.color.background_material_light_darker
    )

    private val PRELOADED_DRAWABLES = intArrayOf(
        R.drawable.bg_button_primary,
        R.drawable.color_primary,
        R.drawable.color_primary_dark,
        R.drawable.bg_button_accent,
        R.drawable.color_accent,
        R.drawable.color_background_dark,
        R.drawable.color_background_dark_lighter,
        R.drawable.color_background_dark_darker,
        R.drawable.color_background_light,
        R.drawable.color_background_light_lighter,
        R.drawable.color_background_light_darker
    )

  }

}