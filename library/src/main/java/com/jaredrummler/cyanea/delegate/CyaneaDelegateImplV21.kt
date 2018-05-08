package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.pm.PackageManager
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
      val icon = activity.packageManager.getApplicationIcon(activity.packageName)
      (icon as? BitmapDrawable)?.bitmap?.let { bitmap ->
        val primary = cyanea.primary
        // Strip out any alpha
        val color = Color.rgb(Color.red(primary), Color.green(primary), Color.blue(primary))
        val td = ActivityManager.TaskDescription(activity.title.toString(), bitmap, color)
        activity.setTaskDescription(td)
      }
    } catch (ignored: PackageManager.NameNotFoundException) {
    }
  }

}