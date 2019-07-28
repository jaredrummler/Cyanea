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
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.getKey
import com.jaredrummler.cyanea.utils.Reflection

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
internal open class CyaneaDelegateImplV24(
  private val activity: Activity,
  private val cyanea: Cyanea,
  themeResId: Int
) : CyaneaDelegateImplV23(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      preloadColors()
    }
  }

  @SuppressLint("PrivateApi")
  private fun preloadColors() {
    try {
      val klass = Class.forName("android.content.res.ColorStateList\$ColorStateListFactory")
      val constructor = klass.getConstructor(ColorStateList::class.java).apply {
        if (!isAccessible) isAccessible = true
      }

      val mResourcesImpl = Reflection.getFieldValue<Any?>(activity.resources, "mResourcesImpl") ?: return
      val cache = Reflection.getFieldValue<Any?>(mResourcesImpl, "sPreloadedComplexColors") ?: return
      val method = Reflection.getMethod(cache, "put", Long::class.java, Object::class.java) ?: return

      for ((id, color) in hashMapOf<Int, Int>().apply {
        put(R.color.cyanea_accent, cyanea.accent)
      }) {
        constructor.newInstance(ColorStateList.valueOf(color))?.let { factory ->
          val key = activity.resources.getKey(id)
          method.invoke(cache, key, factory)
        }
      }
    } catch (ex: Throwable) {
      Cyanea.log(TAG, "Error preloading colors", ex)
    }
  }

  companion object {
    private const val TAG = "CyaneaDelegateImplV24"
  }
}
