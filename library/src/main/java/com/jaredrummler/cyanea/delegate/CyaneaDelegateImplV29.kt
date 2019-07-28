package com.jaredrummler.cyanea.delegate

import android.annotation.TargetApi
import android.app.Activity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R

@RequiresApi(Build_VERSION_CODES_Q)
@TargetApi(Build_VERSION_CODES_Q)
internal open class CyaneaDelegateImplV29(
  activity: Activity,
  private val cyanea: Cyanea,
  themeResId: Int
) : CyaneaDelegateImplV26(activity, cyanea, themeResId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!cyanea.isThemeModified) {
      // https://github.com/jaredrummler/Cyanea/issues/62
      val defaultPrimaryDark = Cyanea.getOriginalColor(R.color.cyanea_default_primary_dark)
      val realPrimaryDark = Cyanea.getOriginalColor(R.color.cyanea_primary_dark_reference)
      if (defaultPrimaryDark == realPrimaryDark) {
        tintBars()
      }
    }
  }

}
