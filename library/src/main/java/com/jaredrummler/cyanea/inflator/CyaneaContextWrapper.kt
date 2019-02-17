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
import android.content.ContextWrapper
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator

/**
 * A [ContextWrapper] that provides a [CyaneaLayoutInflater].
 */
class CyaneaContextWrapper(context: Context,
    private val decorators: Array<CyaneaDecorator>? = null,
    private val viewFactory: CyaneaViewFactory? = null)
  : ContextWrapper(context) {

  private val inflater: CyaneaLayoutInflater by lazy {
    CyaneaLayoutInflater(this).apply {
      this.decorators = this@CyaneaContextWrapper.decorators
      this.viewFactory = this@CyaneaContextWrapper.viewFactory
    }
  }

  override fun getSystemService(name: String): Any? = when (name) {
    LAYOUT_INFLATER_SERVICE -> inflater
    else -> super.getSystemService(name)
  }

}