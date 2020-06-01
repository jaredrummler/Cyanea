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

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.getKey
import com.jaredrummler.cyanea.utils.ColorUtils
import com.jaredrummler.cyanea.utils.Reflection.Companion.getFieldValue
import com.jaredrummler.cyanea.utils.Reflection.Companion.getMethod

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal open class CyaneaDelegateImplV21(
  private val activity: Activity,
  private val cyanea: Cyanea,
  themeResId: Int
) : CyaneaDelegateImplV19(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.LOLLIPOP,
        Build.VERSION_CODES.LOLLIPOP_MR1 -> {
          preloadColors()
        }
      }
    }
  }

  override fun onStart() {
    // Do not call super
    if (cyanea.isThemeModified) {
      // Set the task description with our custom primary color
      setTaskDescription()
    }
  }

  private fun setTaskDescription() {
    try {
      val color = ColorUtils.stripAlpha(cyanea.primary)
      val componentName = ComponentName(activity, activity::class.java)
      activity.packageManager.getActivityInfo(componentName, 0)
        .iconResource.takeIf { it != 0 }?.let { iconRes ->
          val td = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityManager.TaskDescription(activity.title.toString(), iconRes, color)
          } else {
            val icon = BitmapFactory.decodeResource(activity.resources, iconRes) ?: return
            @Suppress("DEPRECATION")
            ActivityManager.TaskDescription(activity.title.toString(), icon, color)
          }
          activity.setTaskDescription(td)
        } ?: run {
        val icon = activity.packageManager.getApplicationIcon(activity.packageName)
        (icon as? BitmapDrawable)?.bitmap?.let { bitmap ->
          @Suppress("DEPRECATION")
          val td = ActivityManager.TaskDescription(activity.title.toString(), bitmap, color)
          activity.setTaskDescription(td)
        }
      }
    } catch (ignored: PackageManager.NameNotFoundException) {
    }
  }

  private fun preloadColors() {
    try {
      val cache = getFieldValue<Any?>(activity.resources, "sPreloadedColorStateLists") ?: return
      val method = getMethod(cache, "put", Long::class.java, Object::class.java) ?: return
      for ((id, color) in hashMapOf<Int, Int>().apply {
        put(R.color.cyanea_accent, cyanea.accent)
      }) {
        val csl = ColorStateList.valueOf(color)
        val key = activity.resources.getKey(id)
        method.invoke(cache, key, csl)
      }
    } catch (ex: Throwable) {
      Cyanea.log(TAG, "Error preloading colors", ex)
    }
  }

  companion object {
    private const val TAG = "CyaneaDelegateImplV21"
  }
}
