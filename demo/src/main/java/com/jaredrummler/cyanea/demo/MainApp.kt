package com.jaredrummler.cyanea.demo

import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaApp

class MainApp : CyaneaApp() {

  override fun onCreate() {
    super.onCreate()
    Cyanea.loggingEnabled = true
    cyanea.edit()
        .primary(0xFF3F51B5.toInt())
        .accent(0xFFFF5722.toInt())
        .background(0xFFEEEEEE.toInt())
        .shouldTintNavBar(true)
        .apply()
  }

}