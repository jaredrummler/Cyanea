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