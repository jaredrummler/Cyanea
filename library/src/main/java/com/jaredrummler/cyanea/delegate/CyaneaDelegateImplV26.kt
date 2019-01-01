package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.tinting.SystemBarTint
import com.jaredrummler.cyanea.utils.ColorUtils

@RequiresApi(Build.VERSION_CODES.O)
@TargetApi(Build.VERSION_CODES.O)
internal open class CyaneaDelegateImplV26(
  private val activity: Activity,
  cyanea: Cyanea,
  themeResId: Int
) : CyaneaDelegateImplV24(activity, cyanea, themeResId) {

  override fun tintNavigationBar(tinter: SystemBarTint, color: Int) {
    super.tintNavigationBar(tinter, color)
    if (!ColorUtils.isDarkColor(color)) {
      activity.window.decorView.run {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      }
    }
  }

  companion object {
    private const val TAG = "CyaneaDelegateImplV26"
  }

}
