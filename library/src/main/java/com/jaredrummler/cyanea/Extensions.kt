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

package com.jaredrummler.cyanea

import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import com.jaredrummler.cyanea.utils.Reflection


internal fun Resources.getKey(id: Int, resolveRefs: Boolean = true) = getValue(id, resolveRefs).let {
  it.assetCookie.toLong() shl 32 or it.data.toLong()
}

internal fun Resources.getValue(id: Int, resolveRefs: Boolean = true) = TypedValue().also { value ->
  (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Reflection.getFieldValue<Any?>(this, "mResourcesImpl")
  } else this)?.run {
    Reflection.invoke<Any?>(this, "getValue",
        arrayOf(Int::class.java, TypedValue::class.java, Boolean::class.java),
        id, value, resolveRefs)
  }
}

internal fun Drawable.setColorFilterCompat(color: Int, mode: PorterDuff.Mode) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    colorFilter = BlendModeColorFilter(color, BlendMode.valueOf(mode.name))
  } else {
    @Suppress("DEPRECATION")
    setColorFilter(color, mode)
  }
}
