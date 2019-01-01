/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.cyanea.delegate

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.getKey
import com.jaredrummler.cyanea.inflator.AlertDialogProcessor
import com.jaredrummler.cyanea.inflator.BottomAppBarProcessor
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
import com.jaredrummler.cyanea.tinting.SystemBarTint
import com.jaredrummler.cyanea.utils.ColorUtils
import com.jaredrummler.cyanea.utils.Reflection

@RequiresApi(Build.VERSION_CODES.M)
@TargetApi(Build.VERSION_CODES.M)
internal open class CyaneaDelegateImplV23(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int
) : CyaneaDelegateImplV21(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      preloadColors()
    }
  }

  @SuppressLint("PrivateApi")
  private fun preloadColors() {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
      try {
        val klass = Class.forName("android.content.res.ColorStateList\$ColorStateListFactory")
        val constructor = klass.getConstructor(ColorStateList::class.java).apply {
          if (!isAccessible) isAccessible = true
        } ?: return

        val cache = Reflection.getFieldValue<Any?>(activity.resources, "sPreloadedColorStateLists") ?: return
        val method = Reflection.getMethod(cache, "put", Long::class.java, Object::class.java) ?: return

        for ((id, color) in hashMapOf<Int, Int>().apply {
          put(R.color.cyanea_accent, cyanea.accent)
          put(R.color.cyanea_primary, cyanea.primary)
        }) {
          try {
            constructor.newInstance(ColorStateList.valueOf(color))?.let { factory ->
              val key = activity.resources.getKey(id)
              method.invoke(cache, key, factory)
            }
          } catch (ex: Throwable) {
            Cyanea.log(TAG, "Error preloading colors", ex)
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

  override fun tintStatusBar(color: Int, tinter: SystemBarTint) {
    super.tintStatusBar(color, tinter)
    if (!ColorUtils.isDarkColor(color)) {
      activity.window.decorView.run {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
        R.color.cyanea_primary,
        R.color.cyanea_primary_light,
        R.color.cyanea_primary_dark,
        R.color.cyanea_accent,
        R.color.cyanea_accent_light,
        R.color.cyanea_accent_dark,
        R.color.cyanea_bg_dark,
        R.color.cyanea_bg_dark_lighter,
        R.color.cyanea_bg_dark_darker,
        R.color.cyanea_bg_light,
        R.color.cyanea_bg_light_lighter,
        R.color.cyanea_bg_light_darker,
        R.color.cyanea_background_dark,
        R.color.cyanea_background_dark_lighter,
        R.color.cyanea_background_dark_darker,
        R.color.cyanea_background_light,
        R.color.cyanea_background_light_lighter,
        R.color.cyanea_background_light_darker
    )

    private val PRELOADED_DRAWABLES = intArrayOf(
        R.drawable.cyanea_bg_button_primary,
        R.drawable.cyanea_primary,
        R.drawable.cyanea_primary_dark,
        R.drawable.cyanea_bg_button_accent,
        R.drawable.cyanea_accent,
        R.drawable.cyanea_bg_dark,
        R.drawable.cyanea_bg_dark_lighter,
        R.drawable.cyanea_bg_dark_darker,
        R.drawable.cyanea_bg_light,
        R.drawable.cyanea_bg_light_lighter,
        R.drawable.cyanea_bg_light_darker
    )

  }

}