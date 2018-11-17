package com.jaredrummler.cyanea.inflator.decor

import android.util.AttributeSet
import android.view.View

/**
 * A class that operates on already constructed views, i.e., decorates them.
 */
interface CyaneaDecorator {

  /**
   * Decorates the given view. This method will be called for every [View] that is created.
   *
   * @param view
   * The view to decorate. Never null.
   * @param attrs
   * A read-only set of tag attributes.
   */
  fun apply(view: View, attrs: AttributeSet)

  /**
   * An interface that may be used in an [activity][android.app.Activity] to provide [decorators][CyaneaDecorator]
   * to the [delegate][com.jaredrummler.cyanea.delegate.CyaneaDelegate]
   */
  interface Provider {

    /**
     * Get an array of [decorators][CyaneaDecorator] to style views.
     *
     * @return An array of decorators for the [delegate][com.jaredrummler.cyanea.delegate.CyaneaDelegate].
     */
    fun getDecorators(): Array<CyaneaDecorator>

  }

}