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

import android.util.AttributeSet
import android.view.View
import com.jaredrummler.cyanea.Cyanea
import java.util.Collections

class CyaneaViewFactory(val cyanea: Cyanea, vararg processors: CyaneaViewProcessor<View>) {

  private val processors = hashSetOf<CyaneaViewProcessor<View>>()

  init {
    Collections.addAll(this.processors, *processors)
  }

  fun onViewCreated(view: View, attrs: AttributeSet): View {
    for (processor in processors) {
      try {
        if (processor.shouldProcessView(view)) {
          processor.process(view, attrs, cyanea)
        }
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error processing view", e)
      }
    }
    return view
  }

  companion object {
    private const val TAG = "CyaneaViewFactory"
  }
}
