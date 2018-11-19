package com.jaredrummler.cyanea.app

import androidx.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.CyaneaThemes
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
   * Get the theme resource id. You can use a pre-defined theme in [CyaneaThemes] or use your own theme that inherits
   * from a Cyanea based theme.
   *
   * @return A cyanea theme
   *
   * @see [CyaneaThemes.actionBarTheme]
   * @see [CyaneaThemes.noActionBarTheme]
   */
  @StyleRes fun getThemeResId(): Int

}