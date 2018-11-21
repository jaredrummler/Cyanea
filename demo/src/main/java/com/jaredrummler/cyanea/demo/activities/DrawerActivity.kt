package com.jaredrummler.cyanea.demo.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.R.id
import com.jaredrummler.cyanea.demo.R.layout
import com.jaredrummler.cyanea.demo.R.string
import com.jaredrummler.cyanea.demo.R.style
import kotlinx.android.synthetic.main.activity_drawer.drawerLayout
import kotlinx.android.synthetic.main.activity_drawer.navigationView
import kotlinx.android.synthetic.main.activity_drawer.toolbar

class DrawerActivity : CyaneaAppCompatActivity() {

  private lateinit var drawerToggle: ActionBarDrawerToggle

  override fun onCreate(savedInstanceState: Bundle?) {

    setTheme(style.Theme_MaterialComponents_Light_NoActionBar)
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_drawer)
    setSupportActionBar(toolbar)

    drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, string.open_drawer,
        string.close_drawer)
    drawerLayout.addDrawerListener(drawerToggle)



    supportActionBar!!.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }

    navigationView.post { navigationView.setCheckedItem(id.item_twitter) }

    navigationView.setNavigationItemSelectedListener {
      navigationView.setCheckedItem(it.itemId)
      false
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    // Sync the toggle state after onRestoreInstanceState has occurred.
    drawerToggle.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    drawerToggle.onConfigurationChanged(newConfig)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
  }

  override fun getThemeResId(): Int = cyanea.themes.noActionBarTheme

}