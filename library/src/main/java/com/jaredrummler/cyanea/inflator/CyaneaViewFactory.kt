package com.jaredrummler.cyanea.inflator

import android.util.AttributeSet
import android.view.View
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.inflator.processors.CyaneaViewProcessor
import java.util.Collections

class CyaneaViewFactory(val cyanea: Cyanea, vararg processors: CyaneaViewProcessor<View>) {

  private val processors = hashSetOf<CyaneaViewProcessor<View>>()

  init {
    Collections.addAll(this.processors, *processors)
  }

  protected fun onViewCreated(view: View, attrs: AttributeSet): View {
    for (processor in processors) {
      try {
        if (processor.shouldProcessView(view)) {
          processor.process(view, attrs, cyanea)
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    return view
  }


}