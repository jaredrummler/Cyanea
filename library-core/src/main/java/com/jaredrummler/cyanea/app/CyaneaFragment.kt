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