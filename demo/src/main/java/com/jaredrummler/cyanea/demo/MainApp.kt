package com.jaredrummler.cyanea.demo

import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaApp
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator
import com.jaredrummler.cyanea.inflator.decor.FontDecorator

class MainApp : CyaneaApp(), CyaneaDecorator.Provider {

  override fun onCreate() {
    super.onCreate()
    Cyanea.loggingEnabled = true
  }

  override fun getDecorators(): Array<CyaneaDecorator> = arrayOf(FontDecorator())

}