package com.jaredrummler.cyanea.inflator.decor

/**
 * An interface that may be used in an [activity][android.app.Activity] to provide [decorators][Decorator]
 * to the [delegate][com.jaredrummler.cyanea.delegate.CyaneaDelegate]
 */
interface DecorProvider {

  /**
   * Get an array of [decorators][Decorator] to style views.
   *
   * @return An array of decorators for the [delegate][com.jaredrummler.cyanea.delegate.CyaneaDelegate].
   */
  fun getDecorators(): Array<Decorator>

}