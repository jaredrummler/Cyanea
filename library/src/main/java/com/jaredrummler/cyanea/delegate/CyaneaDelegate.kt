/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.cyanea.delegate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.inflator.CyaneaContextWrapper
import com.jaredrummler.cyanea.inflator.CyaneaViewFactory
import com.jaredrummler.cyanea.inflator.CyaneaViewProcessor
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator

internal const val Build_VERSION_CODES_Q = 29

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

  protected abstract fun getViewProcessors(): Array<CyaneaViewProcessor<View>>

  protected abstract fun getDecorators(): Array<CyaneaDecorator>

  @StyleRes protected abstract fun getThemeResId(): Int

  companion object {

    /**
     * Create a [CyaneaDelegate] to be used in an [Activity].
     *
     * @param activity The activity
     * @param cyanea The cyanea instance for theming
     * @param themeResId The theme resource id
     * @return The delegate
     */
    @SuppressLint("NewApi") // Needed for Android Pie (API 28) for whatever reason ¯\_(ツ)_/¯
    @JvmStatic
    fun create(activity: Activity, cyanea: Cyanea, @StyleRes themeResId: Int): CyaneaDelegate {
      return when {
        Build.VERSION.SDK_INT >= Build_VERSION_CODES_Q -> CyaneaDelegateImplV29(activity, cyanea, themeResId)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> CyaneaDelegateImplV26(activity, cyanea, themeResId)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> CyaneaDelegateImplV24(activity, cyanea, themeResId)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> CyaneaDelegateImplV23(activity, cyanea, themeResId)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> CyaneaDelegateImplV21(activity, cyanea, themeResId)
        else -> CyaneaDelegateImplBase(activity, cyanea, themeResId)
      }
    }
  }
}
