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
