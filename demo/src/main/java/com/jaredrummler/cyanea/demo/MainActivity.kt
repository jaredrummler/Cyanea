package com.jaredrummler.cyanea.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.R.string
import com.jaredrummler.cyanea.demo.themes.ThemePickerActivity


class MainActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.add(0, 1, 0, getString(string.share))
        .setIcon(R.drawable.ic_share_white_24dp)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

    menu.add(0, 2, 0, getString(string.theme_picker))
        .setIcon(R.drawable.ic_brush_white_24dp)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      1 -> {

      }
      2 -> {
        startActivity(Intent(this, ThemePickerActivity::class.java))
      }
      else -> {
      }
    }


    return super.onOptionsItemSelected(item)
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}
