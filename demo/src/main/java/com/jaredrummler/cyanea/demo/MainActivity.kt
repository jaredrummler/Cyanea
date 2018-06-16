package com.jaredrummler.cyanea.demo

import android.content.Intent
import android.net.Uri
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
    menu.add(0, MENU_GITHUB, 0, getString(string.github))
        .setIcon(R.drawable.ic_github_white_24dp)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

    menu.add(0, MENU_THEMES, 0, getString(string.theme_picker))
        .setIcon(R.drawable.ic_brush_white_24dp)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      MENU_GITHUB -> {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jaredrummler/Cyanea")))
        true
      }
      MENU_THEMES -> {
        startActivity(Intent(this, ThemePickerActivity::class.java))
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

  companion object {
    private const val MENU_GITHUB = Menu.FIRST
    private const val MENU_THEMES = Menu.FIRST + 1
  }

}
