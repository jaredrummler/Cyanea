package com.jaredrummler.cyanea.delegate

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.StyleRes
import android.view.Menu
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.inflator.CyaneaViewFactory
import com.jaredrummler.cyanea.inflator.decor.Decorator
import com.jaredrummler.cyanea.inflator.processors.CyaneaViewProcessor

/**
 * This class represents a delegate which you can use to extend [Cyanea]'s support to any [Activity].
 *
 * When using a [delegate][CyaneaDelegate] the following methods should be called in the corresponding activity:
 *
 * * [CyaneaDelegate.onCreate]
 * * [CyaneaDelegate.onPostCreate]
 * * [CyaneaDelegate.onStart]
 * * [CyaneaDelegate.onResume]
 * * [CyaneaDelegate.onCreateOptionsMenu]
 *
 * The method [CyaneaDelegate.wrap] should also be used in [Activity.attachBaseContext].
 */
abstract class CyaneaDelegate {

  /**
   * Wrap the context in a [CyaneaContextWrapper].
   *
   * @param newBase The base context
   * @return The wrapped context
   */
  abstract fun wrap(newBase: Context): Context

  /**
   * Should be called from [Activity.onCreate()][Activity.onCreate].
   *
   * This should be called before `super.onCreate()` as so:
   *
   * ```
   * override fun onCreate(savedInstanceState: Bundle?) {
   *     getCyaneaDelegate().onCreate(savedInstanceState)
   *     super.onCreate(savedInstanceState)
   *     // ...
   * }
   * ```
   */
  abstract fun onCreate(savedInstanceState: Bundle?)

  /**
   * Should be called from [Activity.onPostCreate]
   */
  abstract fun onPostCreate(savedInstanceState: Bundle?)

  /**
   * Should be called from [Activity.onStart]
   */
  abstract fun onStart()

  /**
   * Should be called from [Activity.onResume()][Activity.onResume].
   *
   * This should be called after `super.onResume()` as so:
   *
   * ```
   * override fun onResume() {
   *     super.onResume()
   *     getCyaneaDelegate().onResume()
   *     // ...
   * }
   * ```
   */
  abstract fun onResume()

  /**
   * Should be called from [Activity.onCreateOptionsMenu] after inflating the menu.
   */
  abstract fun onCreateOptionsMenu(menu: Menu)

  protected abstract fun getViewFactory(): CyaneaViewFactory

  protected abstract fun getViewProcessors(): Array<CyaneaViewProcessor<*>>

  protected abstract fun getDecorators(): Array<Decorator>

  companion object {

    /**
     * Create a [CyaneaDelegate] to be used in an [Activity].
     *
     * @param activity The activity
     * @param cyanea The cyanea instance for theming
     * @param themeResId The theme resource id
     * @return The delegate
     */
    fun create(activity: Activity, cyanea: Cyanea, @StyleRes themeResId: Int): CyaneaDelegate {
      TODO("Create delegate")
    }

  }

}