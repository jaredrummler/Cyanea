package com.jaredrummler.cyanea.inflator

import android.content.Context
import android.content.ContextWrapper
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator

/**
 * A [ContextWrapper] that provides a [CyaneaLayoutInflater].
 */
class CyaneaContextWrapper(context: Context,
    private val decorators: Array<CyaneaDecorator>? = null,
    private val viewFactory: CyaneaViewFactory? = null)
  : ContextWrapper(context) {

  private val inflater: CyaneaLayoutInflater by lazy {
    CyaneaLayoutInflater(this).apply {
      this.decorators = this@CyaneaContextWrapper.decorators
      this.viewFactory = this@CyaneaContextWrapper.viewFactory
    }
  }

  override fun getSystemService(name: String): Any = when (name) {
    LAYOUT_INFLATER_SERVICE -> inflater
    else -> super.getSystemService(name)
  }

}