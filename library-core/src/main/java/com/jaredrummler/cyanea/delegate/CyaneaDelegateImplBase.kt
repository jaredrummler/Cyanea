package com.jaredrummler.cyanea.delegate

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.StyleRes
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.inflator.AlertDialogProcessor
import com.jaredrummler.cyanea.inflator.BottomAppBarProcessor
import com.jaredrummler.cyanea.inflator.CyaneaContextWrapper
import com.jaredrummler.cyanea.inflator.CyaneaViewFactory
import com.jaredrummler.cyanea.inflator.CyaneaViewProcessor
import com.jaredrummler.cyanea.inflator.FloatingActionButtonProcessor
import com.jaredrummler.cyanea.inflator.ListMenuItemViewProcessor
import com.jaredrummler.cyanea.inflator.NavigationViewProcessor
import com.jaredrummler.cyanea.inflator.TextInputLayoutProcessor
import com.jaredrummler.cyanea.inflator.TextViewProcessor
import com.jaredrummler.cyanea.inflator.decor.CyaneaDecorator
import com.jaredrummler.cyanea.tinting.EdgeEffectTint
import com.jaredrummler.cyanea.tinting.MenuTint
import com.jaredrummler.cyanea.tinting.SystemBarTint

internal open class CyaneaDelegateImplBase(
    private val activity: Activity,
    private val cyanea: Cyanea,
    @StyleRes private var themeResId: Int
) : CyaneaDelegate() {

  private val timestamp = cyanea.timestamp

  override fun wrap(newBase: Context): Context {
    return CyaneaContextWrapper(newBase, getDecorators(), getViewFactory())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (cyanea.isThemeModified && getThemeResId() != 0) {
      activity.setTheme(themeResId)
    }
    if (cyanea.isThemeModified) {
      tintBars()
    } else {
      // We use a transparent primary dark color so the library user
      // is not required to specify a color value for cyanea_default_primary_dark
      // If the theme is using the transparent (fake) primary dark color, we need
      // to update the status bar background with the auto-generated primary
      // dark color.
      val defaultPrimaryDark = Cyanea.getOriginalColor(R.color.cyanea_default_primary_dark)
      val realPrimaryDark = Cyanea.getOriginalColor(R.color.cyanea_primary_dark_reference)
      if (defaultPrimaryDark == realPrimaryDark) {
        tintStatusBar()
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
      recreateActivity()
      if (activity is Cyanea.ThemeModifiedListener) {
        activity.onThemeModified()
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu) {
    cyanea.tint(menu, activity)
  }

  override fun getViewFactory(): CyaneaViewFactory {
    val processors = getViewProcessors()
    return CyaneaViewFactory(cyanea, *processors)
  }

  override fun getViewProcessors(): Array<CyaneaViewProcessor<View>> {
    val processors = mutableListOf<CyaneaViewProcessor<View>>()
    // Add processors needed for tinting
    if (cyanea.isThemeModified) {
      processors.addAll(getProcessorsForTheming().filterIsInstance<CyaneaViewProcessor<View>>())
    }
    // Add processors from application
    ((activity.application ?: Cyanea.app) as? CyaneaViewProcessor.Provider)?.let { provider ->
      processors.addAll(provider.getViewProcessors().filterIsInstance<CyaneaViewProcessor<View>>())
    }
    // Add processors from activity
    (activity as? CyaneaViewProcessor.Provider)?.let { provider ->
      processors.addAll(provider.getViewProcessors().filterIsInstance<CyaneaViewProcessor<View>>())
    }
    return processors.toTypedArray()
  }

  override fun getDecorators(): Array<CyaneaDecorator> {
    val decorators = mutableListOf<CyaneaDecorator>()

    // Add decorators from activity
    if (activity is CyaneaDecorator.Provider) {
      activity.getDecorators().forEach { decorators.add(it) }
    }

    // Add decorators from application
    ((activity.application ?: Cyanea.app) as? CyaneaDecorator.Provider)?.apply {
      this.getDecorators().forEach { decorators.add(it) }
    }

    return decorators.toTypedArray()
  }

  protected open fun recreateActivity() = activity.recreate()

  protected open fun tintBars() {
    SystemBarTint(activity).run {
      setActionBarColor(cyanea.primary)
      if (cyanea.shouldTintStatusBar) {
        setStatusBarColor(cyanea.primaryDark)
      }
      if (cyanea.shouldTintNavBar) {
        setNavigationBarColor(cyanea.navigationBar)
      }
    }
  }

  protected open fun tintStatusBar() = SystemBarTint(activity).setStatusBarColor(cyanea.primaryDark)

  protected open fun getProcessorsForTheming(): List<CyaneaViewProcessor<out View>> {
    return arrayListOf(
        ListMenuItemViewProcessor(),
        AlertDialogProcessor(),
        TextViewProcessor(),
        BottomAppBarProcessor(),
        FloatingActionButtonProcessor(),
        TextInputLayoutProcessor(),
        NavigationViewProcessor()
    )
  }

  @StyleRes override fun getThemeResId(): Int {
    if (themeResId == 0) {
      activity.theme?.obtainStyledAttributes(intArrayOf(R.attr.windowActionBar))?.let { styledAttrs ->
        val windowActionBar = styledAttrs.getBoolean(0, true)
        themeResId = if (windowActionBar) {
          cyanea.themes.actionBarTheme
        } else {
          cyanea.themes.noActionBarTheme
        }
      } ?: run {
        Cyanea.log(TAG, "Error getting styled attribute: 'windowActionBar'")
      }
    }
    return themeResId
  }

  companion object {
    private const val TAG = "CyaneaDelegateImplBase"
  }

}