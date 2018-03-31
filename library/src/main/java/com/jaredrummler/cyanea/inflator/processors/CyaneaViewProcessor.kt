package com.jaredrummler.cyanea.inflator.processors

import android.util.AttributeSet
import android.view.View
import com.jaredrummler.cyanea.Cyanea

abstract class CyaneaViewProcessor<T : View> {

  /**
   * Process a newly created view.
   *
   * @param view
   * The newly created view.
   * @param attrs
   * The view's [attributes][AttributeSet]
   * @param radiant
   * The [cyanea][Cyanea] instance used for styling views.
   */
  abstract fun process(view: T, attrs: AttributeSet?, cyanea: Cyanea)

  /**
   * Check if a view should be processed. By default, this checks if the view is an instance of [.getType].
   *
   * @param view
   * The view to check
   * @return [true] if this view should be processed.
   */
  open fun shouldProcessView(view: View): Boolean {
    return getType().isInstance(view)
  }

  /**
   * The class for the given view
   *
   * @return The class for T
   */
  protected abstract fun getType(): Class<T>

}