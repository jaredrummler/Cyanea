package com.jaredrummler.cyanea.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity

class MainActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.add(0, 1, 0, "Menu Item #1")
        .setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

    menu.add(0, 2, 0, "Menu Item #2")
        .setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    menu.add(0, 3, 0, "Menu Item #3")
        .setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    return super.onCreateOptionsMenu(menu)
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}
