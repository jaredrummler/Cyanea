package com.jaredrummler.cyanea

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.Keep
import android.util.Log
import com.jaredrummler.cyanea.Cyanea.BaseTheme.DARK
import com.jaredrummler.cyanea.Cyanea.BaseTheme.LIGHT
import com.jaredrummler.cyanea.tinting.CyaneaTinter
import com.jaredrummler.cyanea.utils.ColorUtils

class Cyanea private constructor(private val prefs: SharedPreferences) {

  @ColorInt var primary: Int
    private set
  @ColorInt var primaryLight: Int
    private set
  @ColorInt var primaryDark: Int
    private set

  @ColorInt var accent: Int
    private set
  @ColorInt var accentLight: Int
    private set
  @ColorInt var accentDark: Int
    private set

  val backgroundColor: Int
    get() = when (baseTheme) {
      LIGHT -> backgroundLight
      DARK -> backgroundDark
    }

  val backgroundColorLight: Int
    get() = when (baseTheme) {
      LIGHT -> backgroundLightLighter
      DARK -> backgroundDarkLighter
    }

  val backgroundColorDark: Int
    get() = when (baseTheme) {
      LIGHT -> backgroundLightDarker
      DARK -> backgroundDarkDarker
    }

  @ColorInt internal var backgroundDark: Int
  @ColorInt internal var backgroundDarkDarker: Int
  @ColorInt internal var backgroundDarkLighter: Int

  @ColorInt internal var backgroundLight: Int
  @ColorInt internal var backgroundLightLighter: Int
  @ColorInt internal var backgroundLightDarker: Int

  var menuIconColor: Int
    private set
  var subMenuIconColor: Int
    private set

  var shouldTintStatusBar: Boolean
    private set
  var shouldTintNavBar: Boolean
    private set

  @ColorInt var navigationBar: Int
    private set

  internal var baseTheme: BaseTheme
  val isDark: Boolean get() = baseTheme == DARK
  val isLight: Boolean get() = baseTheme == LIGHT

  internal var timestamp: Long
    private set

  val isThemeModified: Boolean get() = timestamp != NONE_TIMESTAMP

  val isActionBarDark: Boolean get() = ColorUtils.isDarkColor(primary, 0.75)

  val isActionBarLight: Boolean get() = !isActionBarDark

  internal val tinter = CyaneaTinter.instance

  val themes = CyaneaThemes(this)

  init {
    baseTheme = getBaseTheme(prefs, res)

    primary = prefs.getInt(PREF_PRIMARY,
        res.getColor(R.color.color_primary_reference))
    primaryDark = prefs.getInt(PREF_PRIMARY_DARK,
        res.getColor(R.color.color_primary_dark_reference))
    primaryLight = prefs.getInt(PREF_PRIMARY_LIGHT,
        res.getColor(R.color.color_primary_light_reference))

    accent = prefs.getInt(PREF_ACCENT,
        res.getColor(R.color.color_accent_reference))
    accentDark = prefs.getInt(PREF_ACCENT_DARK,
        res.getColor(R.color.color_accent_dark_reference))
    accentLight = prefs.getInt(PREF_ACCENT_LIGHT,
        res.getColor(R.color.color_accent_light_reference))

    backgroundLight = prefs.getInt(PREF_BACKGROUND_LIGHT,
        res.getColor(R.color.color_background_light))
    backgroundLightDarker = prefs.getInt(PREF_BACKGROUND_LIGHT_DARKER,
        res.getColor(R.color.color_background_light_darker))
    backgroundLightLighter = prefs.getInt(PREF_BACKGROUND_LIGHT_LIGHTER,
        res.getColor(R.color.color_background_light_lighter))

    backgroundDark = prefs.getInt(PREF_BACKGROUND_DARK,
        res.getColor(R.color.color_background_dark))
    backgroundDarkDarker = prefs.getInt(PREF_BACKGROUND_DARK_DARKER,
        res.getColor(R.color.color_background_dark_darker))
    backgroundDarkLighter = prefs.getInt(PREF_BACKGROUND_DARK_LIGHTER,
        res.getColor(R.color.color_background_dark_lighter))

    menuIconColor = prefs.getInt(PREF_MENU_ICON_COLOR,
        res.getColor(if (isActionBarLight) R.color.menu_icon_color_dark else R.color.menu_icon_color_light))
    subMenuIconColor = prefs.getInt(PREF_SUB_MENU_ICON_COLOR,
        res.getColor(if (baseTheme == LIGHT) R.color.sub_menu_icon_color_dark else R.color.sub_menu_icon_color_light))

    navigationBar = prefs.getInt(PREF_NAVIGATION_BAR,
        res.getColor(R.color.color_navigation_bar_reference))

    shouldTintStatusBar = prefs.getBoolean(PREF_SHOULD_TINT_STATUS_BAR,
        res.getBoolean(R.bool.should_tint_status_bar))
    shouldTintNavBar = prefs.getBoolean(PREF_SHOULD_TINT_NAV_BAR,
        res.getBoolean(R.bool.should_tint_nav_bar))

    timestamp = prefs.getLong(PREF_TIMESTAMP, NONE_TIMESTAMP)
  }

  fun edit(): Editor = Editor(this)

  inline fun edit(action: Cyanea.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
  }

  companion object {
    private const val PREF_BASE_THEME = "base_theme"

    private const val PREF_PRIMARY = "primary"
    private const val PREF_PRIMARY_DARK = "primary_dark"
    private const val PREF_PRIMARY_LIGHT = "primary_light"

    private const val PREF_ACCENT = "accent"
    private const val PREF_ACCENT_DARK = "accent_dark"
    private const val PREF_ACCENT_LIGHT = "accent_light"

    private const val PREF_BACKGROUND_LIGHT = "background_light"
    private const val PREF_BACKGROUND_LIGHT_DARKER = "background_light_darker"
    private const val PREF_BACKGROUND_LIGHT_LIGHTER = "background_light_lighter"

    private const val PREF_BACKGROUND_DARK = "background_dark"
    private const val PREF_BACKGROUND_DARK_DARKER = "background_dark_darker"
    private const val PREF_BACKGROUND_DARK_LIGHTER = "background_dark_lighter"

    private const val PREF_MENU_ICON_COLOR = "menu_icon_color"
    private const val PREF_SUB_MENU_ICON_COLOR = "sub_menu_icon_color"

    private const val PREF_NAVIGATION_BAR = "navigation_bar_color"

    private const val PREF_SHOULD_TINT_STATUS_BAR = "should_tint_status_bar"
    private const val PREF_SHOULD_TINT_NAV_BAR = "should_tint_nav_bar"

    private const val PREF_TIMESTAMP = "timestamp"
    private const val NONE_TIMESTAMP = 0L

    private const val DEFAULT_DARKER_FACTOR = 0.85f
    private const val DEFAULT_LIGHTER_FACTOR = 0.15f
    private const val LIGHT_ACTIONBAR_LUMINANCE_FACTOR = 0.75

    @SuppressLint("StaticFieldLeak") // application context is safe
    lateinit var app: Application
    lateinit var res: Resources

    @JvmStatic
    fun init(app: Application, res: Resources) {
      this.app = app
      this.res = res
    }

    @JvmStatic
    fun isInitialized(): Boolean {
      return try {
        app
        res
        true
      } catch (e: UninitializedPropertyAccessException) {
        false
      }
    }

    private object Holder {
      val INSTANCE: Cyanea
        get() {
          try {
            val preferences = app.getSharedPreferences("CYANEA", Context.MODE_PRIVATE)
            return Cyanea(preferences)
          } catch (e: UninitializedPropertyAccessException) {
            throw IllegalStateException("Cyanea.init must be called before referencing the singleton instance")
          }
        }
    }

    @JvmStatic
    val instance: Cyanea by lazy { Holder.INSTANCE }

    var loggingEnabled: Boolean = false

    fun log(tag: String, msg: String, ex: Throwable? = null) {
      if (loggingEnabled) {
        Log.d(tag, msg, ex)
      }
    }

    private fun getBaseTheme(prefs: SharedPreferences, res: Resources): BaseTheme {
      val themeName = prefs.getString(PREF_BASE_THEME, null)
      return when (themeName) {
        LIGHT.name -> LIGHT
        DARK.name -> DARK
        else -> {
          if (res.getBoolean(R.bool.is_default_theme_light)) LIGHT else DARK
        }
      }
    }

  }

  class Editor internal constructor(private val cyanea: Cyanea) {

    private val editor = cyanea.prefs.edit()

    fun primary(@ColorInt color: Int): Editor {
      cyanea.primary = color
      editor.putInt(PREF_PRIMARY, color)
      val isDarkColor = ColorUtils.isDarkColor(color, LIGHT_ACTIONBAR_LUMINANCE_FACTOR)
      val menuIconColorRes = if (isDarkColor) R.color.menu_icon_color_light else R.color.menu_icon_color_dark
      primaryDark(ColorUtils.darker(color, DEFAULT_DARKER_FACTOR))
      primaryLight(ColorUtils.lighter(color, DEFAULT_LIGHTER_FACTOR))
      menuIconColor(res.getColor(menuIconColorRes))
      navigationBar(if (isDarkColor) color else Color.BLACK)
      return this
    }

    fun primaryDark(@ColorInt color: Int): Editor {
      cyanea.primaryDark = color
      editor.putInt(PREF_PRIMARY_DARK, color)
      return this
    }

    fun primaryLight(@ColorInt color: Int): Editor {
      cyanea.primaryLight = color
      editor.putInt(PREF_PRIMARY_LIGHT, color)
      return this
    }

    fun accent(@ColorInt color: Int): Editor {
      cyanea.accent = color
      editor.putInt(PREF_ACCENT, color)
      accentDark(ColorUtils.darker(color, DEFAULT_DARKER_FACTOR))
      accentLight(ColorUtils.lighter(color, DEFAULT_LIGHTER_FACTOR))
      return this
    }

    fun accentDark(@ColorInt color: Int): Editor {
      cyanea.accentDark = color
      editor.putInt(PREF_ACCENT_DARK, color)
      return this
    }

    fun accentLight(@ColorInt color: Int): Editor {
      cyanea.accentLight = color
      editor.putInt(PREF_ACCENT_LIGHT, color)
      return this
    }

    fun background(@ColorInt color: Int): Editor {
      val lighter = ColorUtils.lighter(color, DEFAULT_LIGHTER_FACTOR)
      val darker = ColorUtils.darker(color, DEFAULT_DARKER_FACTOR)
      val isDarkColor = ColorUtils.isDarkColor(color, LIGHT_ACTIONBAR_LUMINANCE_FACTOR)
      if (isDarkColor) {
        baseTheme(DARK)
        backgroundDark(color)
        backgroundDarkDarker(darker)
        backgroundDarkLighter(lighter)
        subMenuIconColor(res.getColor(R.color.sub_menu_icon_color_light))
      } else {
        baseTheme(LIGHT)
        backgroundLight(color)
        backgroundLightDarker(darker)
        backgroundLightLighter(lighter)
        subMenuIconColor(res.getColor(R.color.sub_menu_icon_color_dark))
      }
      return this
    }

    fun backgroundLight(@ColorInt color: Int): Editor {
      cyanea.backgroundLight = color
      editor.putInt(PREF_BACKGROUND_LIGHT, color)
      return this
    }

    fun backgroundLightDarker(@ColorInt color: Int): Editor {
      cyanea.backgroundDarkDarker = color
      editor.putInt(PREF_BACKGROUND_LIGHT_DARKER, color)
      return this
    }

    fun backgroundLightLighter(@ColorInt color: Int): Editor {
      cyanea.backgroundLightLighter = color
      editor.putInt(PREF_BACKGROUND_LIGHT_LIGHTER, color)
      return this
    }

    fun backgroundDark(@ColorInt color: Int): Editor {
      cyanea.backgroundDark = color
      editor.putInt(PREF_BACKGROUND_DARK, color)
      return this
    }

    fun backgroundDarkDarker(@ColorInt color: Int): Editor {
      cyanea.backgroundDarkDarker = color
      editor.putInt(PREF_BACKGROUND_DARK_DARKER, color)
      return this
    }

    fun backgroundDarkLighter(@ColorInt color: Int): Editor {
      cyanea.backgroundDarkLighter = color
      editor.putInt(PREF_BACKGROUND_DARK_LIGHTER, color)
      return this
    }

    fun menuIconColor(@ColorInt color: Int): Editor {
      cyanea.menuIconColor = color
      editor.putInt(PREF_MENU_ICON_COLOR, color)
      return this
    }

    fun subMenuIconColor(@ColorInt color: Int): Editor {
      cyanea.subMenuIconColor = color
      editor.putInt(PREF_SUB_MENU_ICON_COLOR, color)
      return this
    }

    fun navigationBar(@ColorInt color: Int): Editor {
      cyanea.navigationBar = color
      editor.putInt(PREF_NAVIGATION_BAR, color)
      return this
    }

    fun shouldTintStatusBar(choice: Boolean): Editor {
      cyanea.shouldTintStatusBar = choice
      editor.putBoolean(PREF_SHOULD_TINT_STATUS_BAR, choice)
      return this
    }

    fun shouldTintNavBar(choice: Boolean): Editor {
      cyanea.shouldTintNavBar = choice
      editor.putBoolean(PREF_SHOULD_TINT_NAV_BAR, choice)
      return this
    }

    fun baseTheme(theme: BaseTheme): Editor {
      cyanea.baseTheme = theme
      editor.putString(PREF_BASE_THEME, theme.name)
      return this
    }

    fun apply() {
      cyanea.timestamp = System.currentTimeMillis()
      editor.putLong(PREF_TIMESTAMP, cyanea.timestamp)
      editor.apply()
    }

  }

  interface ThemeModifiedListener {

    fun onThemeModified()
  }

  @Keep
  enum class BaseTheme {
    LIGHT,
    DARK
  }

}