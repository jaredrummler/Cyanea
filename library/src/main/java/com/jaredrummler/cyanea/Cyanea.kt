package com.jaredrummler.cyanea

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.support.annotation.ColorInt
import android.support.annotation.Keep
import android.util.Log
import com.jaredrummler.cyanea.Cyanea.BaseTheme.DARK
import com.jaredrummler.cyanea.Cyanea.BaseTheme.LIGHT

class Cyanea private constructor(private val prefs: SharedPreferences) {

  internal var baseTheme: BaseTheme

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

  @ColorInt internal var backgroundDark: Int
  @ColorInt internal var backgroundDarkDarker: Int
  @ColorInt internal var backgroundDarkLighter: Int

  @ColorInt internal var backgroundLight: Int
  @ColorInt internal var backgroundLightLighter: Int
  @ColorInt internal var backgroundLightDarker: Int

  var timestamp: Long
    private set

  val isThemeModified: Boolean
    get() = timestamp == NONE_TIMESTAMP

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

    timestamp = prefs.getLong(PREF_TIMESTAMP, NONE_TIMESTAMP)
  }

  fun edit(): Editor = Editor(this)

  companion object {
    private val PREF_BASE_THEME = "base_theme"

    private val PREF_PRIMARY = "primary"
    private val PREF_PRIMARY_DARK = "primary_dark"
    private val PREF_PRIMARY_LIGHT = "primary_light"

    private val PREF_ACCENT = "accent"
    private val PREF_ACCENT_DARK = "accent_dark"
    private val PREF_ACCENT_LIGHT = "accent_light"

    private val PREF_BACKGROUND_LIGHT = "background_light"
    private val PREF_BACKGROUND_LIGHT_DARKER = "background_light_darker"
    private val PREF_BACKGROUND_LIGHT_LIGHTER = "background_light_lighter"

    private val PREF_BACKGROUND_DARK = "background_dark"
    private val PREF_BACKGROUND_DARK_DARKER = "background_dark_darker"
    private val PREF_BACKGROUND_DARK_LIGHTER = "background_dark_lighter"

    private val PREF_TIMESTAMP = "timestamp"
    private val NONE_TIMESTAMP = 0L

    @SuppressLint("StaticFieldLeak") // application context is safe
    lateinit var app: Application
    lateinit var res: Resources

    fun init(app: Application, res: Resources) {
      this.app = app
      this.res = res
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
      return when {
        LIGHT.name == themeName -> LIGHT
        DARK.name == themeName -> DARK
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

  @Keep
  enum class BaseTheme {
    LIGHT,
    DARK
  }

}
