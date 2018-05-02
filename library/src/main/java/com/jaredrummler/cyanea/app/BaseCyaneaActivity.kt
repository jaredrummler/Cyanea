package com.jaredrummler.cyanea.app

import android.support.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.delegate.CyaneaDelegate

/**
 * Implement in each [activity][android.app.Activity] to provide a [delegate][CyaneaDelegate] for custom theming.
 */
interface BaseCyaneaActivity {

  /**
   * Get the theme resource id
   *
   * @return A cyanea theme
   *
   * @see [CyaneaThemes.getActionBarTheme]
   * @see [CyaneaThemes.getNoActionBarOverlayTheme]
   * @see [CyaneaThemes.getOverlayTheme]
   * @see [CyaneaThemes.getNoActionBarOverlayTheme]
   */
  @StyleRes
  fun getThemeResId(): Int

  /**
   * Get the [Cyanea] instance used for styling.
   *
   * @return The cyanea instance.
   * @see [Cyanea.instance]
   */
  fun getCyanea(): Cyanea

  /**
   * Get the [delegate][CyaneaDelegate] to apply custom themes and decor.
   *
   * @return The delegate.
   * @see [CyaneaDelegate.create]
   */
  fun getCyaneaDelegate(): CyaneaDelegate

}