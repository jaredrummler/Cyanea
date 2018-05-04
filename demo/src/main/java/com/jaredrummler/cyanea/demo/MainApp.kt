package com.jaredrummler.cyanea.demo

import android.graphics.Color
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaApp
import com.jaredrummler.cyanea.utils.ColorUtils

class MainApp : CyaneaApp() {

  override fun onCreate() {
    super.onCreate()
    Cyanea.loggingEnabled = true
    cyanea.edit()
        .primary(Color.CYAN)
        .primaryDark(ColorUtils.darker(Color.CYAN, 0.75f))
        .accent(Color.RED)
        .apply()
  }

}