package com.jaredrummler.cyanea.demo

import android.os.Bundle
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity

class MainActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun getThemeResId(): Int {
    return getCyanea().themes.actionBarTheme
  }

}
