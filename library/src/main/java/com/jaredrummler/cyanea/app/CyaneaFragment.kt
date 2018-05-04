package com.jaredrummler.cyanea.app

import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.tinting.MenuTint

open class CyaneaFragment : Fragment() {

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
    applyMenuTint(menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  open protected fun applyMenuTint(menu: Menu) {
    val cyanea = getCyanea()
    MenuTint(menu,
        menuIconColor = cyanea.menuIconColor,
        subIconColor = cyanea.subMenuIconColor,
        forceIcons = true)
        .apply(activity!!)
  }

  /**
   * Get the [cyanea][Cyanea] instance used by the activity that is associated with this fragment.
   *
   * @return The cyanea instance associated with this fragment
   */
  open fun getCyanea(): Cyanea {
    return (activity as? BaseCyaneaActivity)?.getCyanea() ?: Cyanea.instance
  }

}