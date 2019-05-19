/*
 * Copyright (C) 2018 Jared Rummler
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

package com.jaredrummler.cyanea.app

import androidx.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaThemes
import com.jaredrummler.cyanea.delegate.CyaneaDelegate

/**
 * Implement in each [activity][android.app.Activity] to provide a [delegate][CyaneaDelegate] for custom theming.
 */
interface BaseCyaneaActivity {

  /**
   * The [Cyanea] instance used for styling.
   */
  val cyanea: Cyanea get() = Cyanea.instance

  /**
   * Get the theme resource id. You can use a pre-defined theme in [CyaneaThemes] or use your own theme that inherits
   * from a Cyanea based theme.
   *
   * If 0 is returned then Cyanea will determine whether to use a NoActionBar theme based on the current theme.
   *
   * @return A cyanea theme
   *
   * @see [CyaneaThemes.actionBarTheme]
   * @see [CyaneaThemes.noActionBarTheme]
   */
  @StyleRes fun getThemeResId(): Int = 0
}
