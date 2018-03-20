package com.jaredrummler.cyanea.inflator.decor

import android.util.AttributeSet
import android.view.View

/**
 * A class that operates on already constructed views, i.e., decorates them.
 */
interface Decorator {

  /**
   * Decorates the given view. This method will be called for every [View] that is created.
   *
   * @param view
   * The view to decorate. Never null.
   * @param attrs
   * A read-only set of tag attributes.
   */
  fun apply(view: View, attrs: AttributeSet)

}