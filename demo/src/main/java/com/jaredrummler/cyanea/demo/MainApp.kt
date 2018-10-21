package com.jaredrummler.cyanea.demo

import android.view.View
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaApp
import com.jaredrummler.cyanea.inflator.CyaneaViewProcessor
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator
import com.jaredrummler.cyanea.inflator.decor.FontDecorator

class MainApp : CyaneaApp(), CyaneaDecorator.Provider, CyaneaViewProcessor.Provider {

  override fun onCreate() {
    super.onCreate()
    Cyanea.loggingEnabled = true
  }

  override fun getViewProcessors(): Array<CyaneaViewProcessor<out View>> = arrayOf()

  override fun getDecorators(): Array<CyaneaDecorator> = arrayOf(FontDecorator())

}