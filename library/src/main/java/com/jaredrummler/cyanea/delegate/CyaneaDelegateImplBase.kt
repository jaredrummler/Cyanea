package com.jaredrummler.cyanea.delegate

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.StyleRes
import android.view.Menu
import android.view.View
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.inflator.CyaneaContextWrapper
import com.jaredrummler.cyanea.inflator.CyaneaViewFactory
import com.jaredrummler.cyanea.inflator.decor.DecorProvider
import com.jaredrummler.cyanea.inflator.decor.Decorator
import com.jaredrummler.cyanea.inflator.processors.AlertDialogProcessor
import com.jaredrummler.cyanea.inflator.processors.CyaneaViewProcessor
import com.jaredrummler.cyanea.inflator.processors.ListMenuItemViewProcessor
import com.jaredrummler.cyanea.inflator.processors.TextViewProcessor
import com.jaredrummler.cyanea.inflator.processors.ViewProcessorProvider
import com.jaredrummler.cyanea.tinting.EdgeEffectTint
import com.jaredrummler.cyanea.tinting.MenuTint
import com.jaredrummler.cyanea.tinting.SystemBarTint

open class CyaneaDelegateImplBase(
    private val activity: Activity,
    private val cyanea: Cyanea,
    @StyleRes private val themeResId: Int)
  : CyaneaDelegate() {

  private val timestamp = cyanea.timestamp

  override fun wrap(newBase: Context): Context {
    return CyaneaContextWrapper(newBase, getDecorators(), getViewFactory())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (themeResId != 0 && cyanea.isThemeModified) {
      activity.setTheme(themeResId)
    }

    if (cyanea.isThemeModified) {
      val tinter = SystemBarTint(activity)
      tinter.setActionBarColor(cyanea.primary)
      if (cyanea.shouldTintStatusBar) {
        tinter.setStatusBarColor(cyanea.primaryDark)
      }
      if (cyanea.shouldTintNavBar) {
        tinter.setNavigationBarColor(cyanea.primary)
      }
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    if (cyanea.isThemeModified) {
      val ta = activity.obtainStyledAttributes(intArrayOf(android.R.attr.windowIsTranslucent))
      try {
        val isTranslucent = ta.getBoolean(0, false)
        if (!isTranslucent) {
          activity.window.setBackgroundDrawable(ColorDrawable(cyanea.backgroundColor))
        }
      } finally {
        ta.recycle()
      }
    }
  }

  override fun onStart() {
    if (cyanea.isThemeModified) {
      EdgeEffectTint(activity).tint(cyanea.primary)
      MenuTint.forceOverflow(activity)
    }
  }

  override fun onResume() {
    if (timestamp != cyanea.timestamp) {
      activity.recreate()
      if (activity is Cyanea.ThemeModifiedListener) {
        activity.onThemeModified()
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu) {
    MenuTint(menu, menuIconColor = cyanea.menuIconColor, subIconColor = cyanea.subMenuIconColor, forceIcons = true)
        .apply(activity)
  }

  override fun getViewFactory(): CyaneaViewFactory {
    val processors = getViewProcessors()
    return CyaneaViewFactory(cyanea, *processors)
  }

  override fun getViewProcessors(): Array<CyaneaViewProcessor<View>> {
    val processors = mutableListOf<CyaneaViewProcessor<View>>()

    // Add processors needed for tinting
    if (cyanea.isThemeModified) {
      getProcessorsForTheming().forEach {
        (it as? CyaneaViewProcessor<View>)?.let { processors.add(it) }
      }
    }

    // Add processors from activity
    if (activity is ViewProcessorProvider) {
      activity.getViewProcessors().forEach {
        (it as? CyaneaViewProcessor<View>)?.let { processors.add(it) }
      }
    }

    // Add processors from application
    (activity.application as? ViewProcessorProvider)?.apply {
      this.getViewProcessors().forEach {
        (it as? CyaneaViewProcessor<View>)?.let { processors.add(it) }
      }
    }

    return processors.toTypedArray()
  }

  override fun getDecorators(): Array<Decorator> {
    val decorators = mutableListOf<Decorator>()

    // Add decorators from activity
    if (activity is DecorProvider) {
      activity.getDecorators().forEach { decorators.add(it) }
    }

    // Add decorators from application
    (activity.application as? DecorProvider)?.apply {
      this.getDecorators().forEach { decorators.add(it) }
    }

    return decorators.toTypedArray()
  }

  protected fun getProcessorsForTheming(): List<CyaneaViewProcessor<*>> {
    return arrayListOf(
        ListMenuItemViewProcessor(),
        AlertDialogProcessor(),
        TextViewProcessor()
    )
  }

}