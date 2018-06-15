package com.jaredrummler.cyanea.demo

import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaApp

class MainApp : CyaneaApp() {

  override fun onCreate() {
    super.onCreate()
    Cyanea.loggingEnabled = true
  }

}