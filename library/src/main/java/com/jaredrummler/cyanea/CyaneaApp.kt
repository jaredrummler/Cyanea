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

package com.jaredrummler.cyanea

import android.app.Application
import android.content.res.Resources

/**
 * A subclass of [Application] which should be added to the AndroidManifest.
 *
 * If you cannot extend from this class you can initialize you must initialize Cyanea from your own base app:
 *
 * ```kotlin
 * override run onCreate() {
 *   super.onCreate()
 *   Cyanea.init(this, super.getResources())
 * }
 * ```
 */
open class CyaneaApp : Application() {

  private val resources: CyaneaResources by lazy {
    CyaneaResources(super.getResources(), cyanea)
  }

  /**
   * The [cyanea][Cyanea] instance used to create the application's resources
   */
  open val cyanea: Cyanea by lazy { Cyanea.instance }

  override fun onCreate() {
    super.onCreate()
    Cyanea.init(this, super.getResources())
  }

  override fun getResources(): Resources {
    return if (Cyanea.isInitialized()) resources else super.getResources()
  }

}