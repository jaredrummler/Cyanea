package com.jaredrummler.cyanea.inflator

import android.content.Context
import android.content.ContextWrapper
import com.jaredrummler.cyanea.inflator.decor.Decorator

/**
 * A [ContextWrapper] that provides a [CyaneaLayoutInflater].
 */
class CyaneaContextWrapper(context: Context,
    private val decorators: Array<Decorator>? = null,
    private val viewFactory: CyaneaViewFactory? = null)
  : ContextWrapper(context) {

  private var inflator: CyaneaLayoutInflater? = null

  override fun getSystemService(name: String?): Any {
    if (LAYOUT_INFLATER_SERVICE == name) {
      if (inflator == null) {
        inflator = CyaneaLayoutInflater(this)
        inflator?.decorators = decorators
        inflator?.viewFactory = viewFactory
      }
      inflator?.let { return it }
    }
    return super.getSystemService(name)
  }

}