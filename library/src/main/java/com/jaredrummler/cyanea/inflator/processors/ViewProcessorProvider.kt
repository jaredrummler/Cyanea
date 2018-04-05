package com.jaredrummler.cyanea.inflator.processors

import com.jaredrummler.cyanea.delegate.CyaneaDelegate

/**
 * An interface that may be used in an [activity][Activity] to provide [view processors][CyaneaViewProcessor]
 * to the [CyaneaDelegate].
 */
interface ViewProcessorProvider {

  /**
   * Get an array of [view processors][CyaneaViewProcessor] to style views.
   *
   * @return An array of decorators for the [CyaneaDelegate].
   */
  fun getViewProcessors(): Array<CyaneaViewProcessor<*>>

}