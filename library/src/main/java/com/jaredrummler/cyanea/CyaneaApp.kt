package com.jaredrummler.cyanea

import android.app.Application
import android.content.res.Resources

open class CyaneaApp : Application() {

  private lateinit var res: CyaneaResources
  private var initialzed: Boolean = false

  override fun onCreate() {
    super.onCreate()
    Cyanea.init(this, super.getResources())
    initialzed = true
  }

  override fun getResources(): Resources {
    if (!initialzed) {
      // Don't give a ContentProvider our custom resources
      return super.getResources()
    }
    if (!::res.isInitialized) {
      res = CyaneaResources(super.getResources(), getCyanea())
    }
    return res
  }

  /**
   * @return The [cyanea][Cyanea] instance used to create the application's resources
   */
  protected open fun getCyanea(): Cyanea = Cyanea.instance

}