package com.jaredrummler.cyanea.prefs

import android.os.Bundle
import android.view.MenuItem
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity

/**
 * Activity to show Cyanea preferences allowing the user to modify the primary, accent and background color of the app.
 */
open class CyaneaSettingsActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(android.R.id.content, CyaneaSettingsFragment.newInstance())
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

}