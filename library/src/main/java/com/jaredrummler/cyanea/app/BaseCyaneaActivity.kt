package com.jaredrummler.cyanea.app

import android.support.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.delegate.CyaneaDelegate

/**
 * Implement in each [activity][android.app.Activity] to provide a [delegate][CyaneaDelegate] for custom theming.
 */
interface BaseCyaneaActivity {

  /**
   * The [Cyanea] instance used for styling.
   */
  val cyanea: Cyanea get() = Cyanea.instance

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
  @StyleRes fun getThemeResId(): Int

}