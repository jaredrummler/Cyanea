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

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.jaredrummler.cyanea.Cyanea

/**
 * Base class for fragments[Fragment] that use [Cyanea] for dynamic themes.
 */
open class CyaneaFragment : Fragment() {

  /**
   * The [Cyanea] instance used for styling.
   */
  open val cyanea: Cyanea get() = (activity as? BaseCyaneaActivity)?.cyanea ?: Cyanea.instance

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
    applyMenuTint(menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  protected open fun applyMenuTint(menu: Menu) = cyanea.tint(menu, requireActivity())

}