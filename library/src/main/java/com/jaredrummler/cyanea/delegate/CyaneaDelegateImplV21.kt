package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal open class CyaneaDelegateImplV21(
    private val activity: Activity,
    private val cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplV19(activity, cyanea, themeResId) {

  override fun onStart() {
    // Do not call super
    if (cyanea.isThemeModified) {
      // Set the task description with our custom primary color
      setTaskDescription()
    }
  }

  private fun setTaskDescription() {
    try {
      val primary = cyanea.primary
      val color = Color.rgb(Color.red(primary), Color.green(primary), Color.blue(primary)) // Strip out any alpha

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
        return
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

}