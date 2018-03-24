package com.jaredrummler.cyanea.demo

import android.graphics.Color
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaApp

class MainApp : CyaneaApp() {

  override fun onCreate() {
    super.onCreate()
    Cyanea.instance.primary = Color.MAGENTA
  }

}