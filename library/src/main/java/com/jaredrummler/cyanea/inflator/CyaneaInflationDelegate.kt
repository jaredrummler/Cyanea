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

package com.jaredrummler.cyanea.inflator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.jaredrummler.cyanea.Cyanea

/**
 * Hook to create custom views at inflation time.
 *
 * @see Cyanea.setInflationDelegate
 */
interface CyaneaInflationDelegate {

  /**
   * Hook you can supply that is called when inflating from a [CyaneaLayoutInflater].
   *
   * @param parent The parent that the created view will be placed in; <em>note that this may be null</em>.
   * @param name Tag name to be inflated.
   * @param context The context the view is being created in.
   * @param attrs Inflation attributes as specified in XML file.
   * @return Newly created view. Return null for the default behavior.
   */
  fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet): View?

}