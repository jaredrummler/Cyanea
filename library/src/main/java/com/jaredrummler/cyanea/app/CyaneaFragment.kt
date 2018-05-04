package com.jaredrummler.cyanea.app

import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.tinting.MenuTint

open class CyaneaFragment : Fragment() {

  /**
   * The [Cyanea] instance used for styling.
   */
  open val cyanea: Cyanea get() = (activity as? BaseCyaneaActivity)?.cyanea ?: Cyanea.instance

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
    applyMenuTint(menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  protected open fun applyMenuTint(menu: Menu) {
    MenuTint(menu,
        menuIconColor = cyanea.menuIconColor,
        subIconColor = cyanea.subMenuIconColor,
        forceIcons = true)
        .apply(activity!!)
  }

}