package com.jaredrummler.cyanea.inflator.processors

import android.support.v7.widget.AlertDialogLayout
import android.util.AttributeSet
import android.view.View
import com.jaredrummler.cyanea.Cyanea

/**
 * A [CyaneaViewProcessor] that themes alert dialog backgrounds.
 */
class AlertDialogProcessor : CyaneaViewProcessor<View>() {

  override fun getType(): Class<View> = View::class.java

  override fun shouldProcessView(view: View): Boolean = view is AlertDialogLayout || CLASS_NAME == view.javaClass.name

  override fun process(view: View, attrs: AttributeSet?, cyanea: Cyanea) {
    view.setBackgroundColor(cyanea.backgroundColor)
  }

  companion object {
    private val CLASS_NAME = "com.android.internal.widget.AlertDialogLayout"
  }

}