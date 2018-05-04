package com.jaredrummler.cyanea.app

import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.tinting.MenuTint

open class CyaneaFragment : Fragment() {

  /**
   * Get the [cyanea][Cyanea] instance used by the activity that is associated with this fragment.
   *
   * @return The cyanea instance associated with this fragment
   */
  open val cyanea: Cyanea get() = (activity as? BaseCyaneaActivity)?.cyanea ?: Cyanea.instance

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
    applyMenuTint(menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  open protected fun applyMenuTint(menu: Menu) {
    MenuTint(menu,
        menuIconColor = cyanea.menuIconColor,
        subIconColor = cyanea.subMenuIconColor,
        forceIcons = true)
        .apply(activity!!)
  }


}