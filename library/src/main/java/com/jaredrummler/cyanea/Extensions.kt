package com.jaredrummler.cyanea

import android.content.res.Resources
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