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
import com.jaredrummler.cyanea.utils.Reflection

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
internal open class CyaneaDelegateImplV24(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplV23(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      preloadAccentColor()
    }
  }

  @SuppressLint("PrivateApi")
  private fun preloadAccentColor() {
    val mResourcesImpl = Reflection.getFieldValue<Any?>(activity.resources, "mResourcesImpl") ?: return

    // Load the TypedValue and key
    val value = TypedValue()
    val getValueMethod = Reflection.getMethod(mResourcesImpl, "getValue",
        Int::class.java, TypedValue::class.java, Boolean::class.java) ?: return
    getValueMethod.invoke(mResourcesImpl, R.color.color_accent, value, true)
    val key = value.assetCookie.toLong() shl 32 or value.data.toLong()

    // Create a ColorStateList for the ColorStateListFactory
    val color = Cyanea.getOriginalColor(R.color.color_accent)
    val csl = ColorStateList.valueOf(color)

    // Add our factory to ResourcesImpl.sPreloadedComplexColors
    // This will use our factory instead of using ResourcesImpl#loadComplexColorForCookie
    // This is needed so the correct accent color is cached
    Class.forName("android.content.res.ColorStateList\$ColorStateListFactory").let { klass ->
      val constructor = klass.getConstructor(ColorStateList::class.java) ?: return
      if (!constructor.isAccessible) constructor.isAccessible = true
      constructor.newInstance(csl)?.let { factory ->
        Reflection.getField(mResourcesImpl, "sPreloadedComplexColors")?.let { field ->
          field.get(null)?.let { factories ->
            Reflection.getMethod(factories, "put", Long::class.java, Object::class.java)
                ?.invoke(factories, key, factory)
          }
        }
      }
    }
  }

}