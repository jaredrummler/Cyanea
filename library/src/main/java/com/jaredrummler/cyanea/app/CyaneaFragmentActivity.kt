package com.jaredrummler.cyanea.app

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.v4.app.FragmentActivity
import android.view.Menu
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaResources
import com.jaredrummler.cyanea.delegate.CyaneaDelegate

abstract class CyaneaFragmentActivity : FragmentActivity(), BaseCyaneaActivity {

  private val delegate: CyaneaDelegate by lazy {
    CyaneaDelegate.create(this, getCyanea(), getThemeResId())
  }

  private val resources: CyaneaResources by lazy {
    CyaneaResources(super.getResources(), getCyanea())
  }

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(delegate.wrap(newBase))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    delegate.onCreate(savedInstanceState)
    super.onCreate(savedInstanceState)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    delegate.onPostCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    delegate.onStart()
  }

  override fun onResume() {
    super.onResume()
    delegate.onResume()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    delegate.onCreateOptionsMenu(menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun getResources(): Resources = resources

  override fun getCyanea(): Cyanea = Cyanea.instance

  override fun getCyaneaDelegate(): CyaneaDelegate = delegate

  @StyleRes abstract override fun getThemeResId(): Int

}