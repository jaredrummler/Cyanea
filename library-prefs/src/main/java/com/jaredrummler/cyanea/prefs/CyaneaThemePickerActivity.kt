package com.jaredrummler.cyanea.prefs

import android.os.Bundle
import android.view.MenuItem
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity

class CyaneaThemePickerActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(android.R.id.content, CyaneaThemePickerFragment())
          .commit()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
    android.R.id.home -> {
      finish()
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}