package com.jaredrummler.cyanea.activity

import android.support.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.delegate.CyaneaDelegate

/**
 * Implement in each [activity][android.app.Activity] to provide a [delegate][CyaneaDelegate] for custom theming.
 */
interface BaseCyaneaActivity {

  @StyleRes
  fun getThemeResId(): Int

  fun getCyanea(): Cyanea

  fun getCyaneaDelegate(): CyaneaDelegate

}