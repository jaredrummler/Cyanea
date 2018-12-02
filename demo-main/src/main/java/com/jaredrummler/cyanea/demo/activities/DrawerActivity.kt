package com.jaredrummler.cyanea.demo.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.R
import kotlinx.android.synthetic.main.activity_drawer.drawerLayout
import kotlinx.android.synthetic.main.activity_drawer.navigationView
import kotlinx.android.synthetic.main.activity_drawer.toolbar

class DrawerActivity : CyaneaAppCompatActivity() {

  private lateinit var drawerToggle: ActionBarDrawerToggle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_drawer)
    setSupportActionBar(toolbar)

    supportActionBar!!.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }

    drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
    drawerLayout.addDrawerListener(drawerToggle)

    navigationView.post { navigationView.setCheckedItem(R.id.item_twitter) }

    navigationView.setNavigationItemSelectedListener {
      navigationView.setCheckedItem(it.itemId)
      false
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    drawerToggle.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    drawerToggle.onConfigurationChanged(newConfig)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
  }

}