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
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.utils.ColorUtils
import com.jaredrummler.cyanea.utils.Reflection

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal open class CyaneaDelegateImplV21(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplV19(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (cyanea.isThemeModified) {
      when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.LOLLIPOP,
        Build.VERSION_CODES.LOLLIPOP_MR1 -> {
          preloadAccentColor()
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
      val activityInfo = activity.packageManager.getActivityInfo(componentName, 0)
      activityInfo?.iconResource.takeIf { it != 0 }?.let { iconRes ->
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

  private fun preloadAccentColor() {
    for (id in arrayOf(
        R.color.color_accent
    )) {
      try {
        val field = Reflection.getField(activity.resources, "sPreloadedColorStateLists") ?: return

        val value = TypedValue()
        val getValueMethod = Reflection.getMethod(activity.resources, "getValue",
            Int::class.java, TypedValue::class.java, Boolean::class.java) ?: return
        getValueMethod.invoke(activity.resources, id, value, true)
        val key = value.assetCookie.toLong() shl 32 or value.data.toLong()

        val csl = ColorStateList.valueOf(cyanea.accent)

        field.get(null)?.let { cache ->
          Reflection.getMethod(cache, "put", Long::class.java, Object::class.java)?.invoke(cache, key, csl)
        }
      } catch (ex: Throwable) {
        Cyanea.log(TAG, "Error preloading colors", ex)
      }
    }
  }

  companion object {
    private const val TAG = "CyaneaDelegateImplV21"
  }

}