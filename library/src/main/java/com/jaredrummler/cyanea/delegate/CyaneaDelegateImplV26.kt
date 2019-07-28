/*
 * Copyright (C) 2019 Jared Rummler
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

  override fun tintNavigationBar(color: Int, tinter: SystemBarTint) {
    super.tintNavigationBar(color, tinter)
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
