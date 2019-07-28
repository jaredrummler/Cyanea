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
