package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea

@RequiresApi(Build.VERSION_CODES.P)
@TargetApi(Build.VERSION_CODES.P)
internal open class CyaneaDelegateImplV28(
    private val activity: Activity,
    cyanea: Cyanea,
    themeResId: Int)
  : CyaneaDelegateImplV24(activity, cyanea, themeResId) {

  override fun recreateActivity() {
    tintBars()
    activity.findViewById<View>(android.R.id.content).post { activity.recreate() }
  }

}