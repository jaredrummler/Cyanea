package com.jaredrummler.cyanea.delegate

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.inflator.AlertDialogProcessor
import com.jaredrummler.cyanea.inflator.BottomAppBarProcessor
import com.jaredrummler.cyanea.inflator.CheckedTextViewProcessor
import com.jaredrummler.cyanea.inflator.CompoundButtonProcessor
import com.jaredrummler.cyanea.inflator.CyaneaViewProcessor
import com.jaredrummler.cyanea.inflator.DatePickerProcessor
import com.jaredrummler.cyanea.inflator.ImageButtonProcessor
import com.jaredrummler.cyanea.inflator.ListMenuItemViewProcessor
import com.jaredrummler.cyanea.inflator.SearchAutoCompleteProcessor
import com.jaredrummler.cyanea.inflator.SwitchCompatProcessor
import com.jaredrummler.cyanea.inflator.SwitchProcessor
import com.jaredrummler.cyanea.inflator.TextViewProcessor
import com.jaredrummler.cyanea.inflator.TimePickerProcessor
import com.jaredrummler.cyanea.inflator.ViewGroupProcessor
import com.jaredrummler.cyanea.utils.Reflection

@RequiresApi(Build.VERSION_CODES.M)
@TargetApi(Build.VERSION_CODES.M)
internal open class CyaneaDelegateImplV23(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplV21(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      preloadColors()


    }
  }

  @SuppressLint("PrivateApi")
  private fun preloadColors() {
    for ((id, color) in hashMapOf<Int, Int>().apply {
      put(R.color.color_accent, cyanea.accent)
      put(R.color.color_primary, cyanea.primary)
    }) {
      if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
        try {
          val field = Reflection.getField(activity.resources, "sPreloadedColorStateLists") ?: return

          val value = TypedValue()
          val getValueMethod = Reflection.getMethod(activity.resources, "getValue",
              Int::class.java, TypedValue::class.java, Boolean::class.java) ?: return
          getValueMethod.invoke(activity.resources, id, value, true)
          val key = value.assetCookie.toLong() shl 32 or value.data.toLong()

          val csl = ColorStateList.valueOf(color)

          Class.forName("android.content.res.ColorStateList\$ColorStateListFactory").let { klass ->
            val constructor = klass.getConstructor(ColorStateList::class.java) ?: return
            if (!constructor.isAccessible) constructor.isAccessible = true
            constructor.newInstance(csl)?.let { factory ->
              field.get(null)?.let { factories ->
                Reflection.getMethod(factories, "put", Long::class.java, Object::class.java)
                    ?.invoke(factories, key, factory)
              }
            }
          }
        } catch (ex: Throwable) {
          Cyanea.log(TAG, "Error preloading colors", ex)
        }
      }

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
    processors.addAll(
        arrayOf(
            AlertDialogProcessor(),
            BottomAppBarProcessor(),
            CheckedTextViewProcessor(),
            CompoundButtonProcessor(),
            DatePickerProcessor(),
            ImageButtonProcessor(),
            ListMenuItemViewProcessor(),
            SearchAutoCompleteProcessor(),
            SwitchProcessor(),
            SwitchCompatProcessor(),
            TimePickerProcessor(),
            TextViewProcessor(),
            ViewGroupProcessor()
        )
    )
    return processors
  }

  companion object {

    private const val TAG = "CyaneaDelegateImplV23"

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