@file:Suppress("DEPRECATION")

package com.jaredrummler.cyanea

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Keep
import com.jaredrummler.cyanea.Constants.LIGHT_ACTIONBAR_LUMINANCE_FACTOR
import com.jaredrummler.cyanea.Constants.NONE_TIMESTAMP
import com.jaredrummler.cyanea.Cyanea.BaseTheme.DARK
import com.jaredrummler.cyanea.Cyanea.BaseTheme.LIGHT
import com.jaredrummler.cyanea.Defaults.DEFAULT_DARKER_FACTOR
import com.jaredrummler.cyanea.Defaults.DEFAULT_LIGHTER_FACTOR
import com.jaredrummler.cyanea.PrefKeys.PREF_ACCENT
import com.jaredrummler.cyanea.PrefKeys.PREF_ACCENT_DARK
import com.jaredrummler.cyanea.PrefKeys.PREF_ACCENT_LIGHT
import com.jaredrummler.cyanea.PrefKeys.PREF_BACKGROUND_DARK
import com.jaredrummler.cyanea.PrefKeys.PREF_BACKGROUND_DARK_DARKER
import com.jaredrummler.cyanea.PrefKeys.PREF_BACKGROUND_DARK_LIGHTER
import com.jaredrummler.cyanea.PrefKeys.PREF_BACKGROUND_LIGHT
import com.jaredrummler.cyanea.PrefKeys.PREF_BACKGROUND_LIGHT_DARKER
import com.jaredrummler.cyanea.PrefKeys.PREF_BACKGROUND_LIGHT_LIGHTER
import com.jaredrummler.cyanea.PrefKeys.PREF_BASE_THEME
import com.jaredrummler.cyanea.PrefKeys.PREF_FILE_NAME
import com.jaredrummler.cyanea.PrefKeys.PREF_MENU_ICON_COLOR
import com.jaredrummler.cyanea.PrefKeys.PREF_NAVIGATION_BAR
import com.jaredrummler.cyanea.PrefKeys.PREF_PRIMARY
import com.jaredrummler.cyanea.PrefKeys.PREF_PRIMARY_DARK
import com.jaredrummler.cyanea.PrefKeys.PREF_PRIMARY_LIGHT
import com.jaredrummler.cyanea.PrefKeys.PREF_SHOULD_TINT_NAV_BAR
import com.jaredrummler.cyanea.PrefKeys.PREF_SHOULD_TINT_STATUS_BAR
import com.jaredrummler.cyanea.PrefKeys.PREF_SUB_MENU_ICON_COLOR
import com.jaredrummler.cyanea.PrefKeys.PREF_TIMESTAMP
import com.jaredrummler.cyanea.tinting.CyaneaTinter
import com.jaredrummler.cyanea.tinting.MenuTint
import com.jaredrummler.cyanea.utils.ColorUtils





/**
 * Contains colors for an application theme.
 *
 * Before using Cyanea you must initialize it in your application class or have the application class be [CyaneaApp].
 *
 * To retrieve a color from a Cyanea based activity, simply call:
 *
 * ```kotlin
 * val primaryColor = cyanea.primary // application's primary color
 * val accentColor = cyanea.accent // application's accent color
 * ```
 *
 * To dynamically edit a theme you can use [Cyanea.Editor].
 *
 * Example:
 *
 * ```kotlin
 * Cyanea.instance.edit {
 *   primary(Color.RED)
 *   accent(Color.YELLOW)
 *   background(Color.BLACK)
 * }
 * ```
 *
 * After editing a theme you must recreate the activity for changes to apply.
 */
class Cyanea private constructor(private val prefs: SharedPreferences) {

  /** The primary color displayed most frequently across your app */
  @ColorInt var primary: Int
    private set
  /** A lighter version of the [primary] color */
  @ColorInt var primaryLight: Int
    private set
  /** A darker version of the [primary] color */
  @ColorInt var primaryDark: Int
    private set

  /** The accent color that accents select parts of the UI */
  @ColorInt var accent: Int
    private set
  /** A lighter version of the [accent] color */
  @ColorInt var accentLight: Int
    private set
  /** A darker version of the [accent] color */
  @ColorInt var accentDark: Int
    private set

  /** The background color used as the underlying color of the app's content */
  val backgroundColor: Int
    get() = when (baseTheme) {
      LIGHT -> backgroundLight
      DARK -> backgroundDark
    }
  /* A lighter version of the [background] color */
  val backgroundColorLight: Int
    get() = when (baseTheme) {
      LIGHT -> backgroundLightLighter
      DARK -> backgroundDarkLighter
    }
  /* A darker version of the [background] color */
  val backgroundColorDark: Int
    get() = when (baseTheme) {
      LIGHT -> backgroundLightDarker
      DARK -> backgroundDarkDarker
    }

  /** The color of icons in a [Menu] */
  @ColorInt var menuIconColor: Int
    private set
  /** The color of icons in a [menu's][Menu] sub-menu */
  @ColorInt var subMenuIconColor: Int
    private set

  /** The color of the navigation bar, usually is black or the [primary] color */
  @ColorInt var navigationBar: Int
    private set
  /** True to set the [primaryDark] color on the system status bar */
  var shouldTintStatusBar: Boolean
    private set
  /** True to set the [navigationBar] color on the system navigation bar */
  var shouldTintNavBar: Boolean
    private set

  /** The base theme. Either [LIGHT] or [DARK] */
  var baseTheme: BaseTheme
    internal set
  /** True if the [baseTheme] is [DARK] */
  val isDark get() = baseTheme == DARK
  /** True if the [baseTheme] is [LIGHT] */
  val isLight get() = baseTheme == LIGHT
  /** True if the [primary] color is a dark color */
  val isActionBarDark get() = ColorUtils.isDarkColor(primary, 0.75)
  /** True if the [primary] color is a light color */
  val isActionBarLight get() = !isActionBarDark
  /** True if the theme has been modified at least once */
  val isThemeModified get() = timestamp != NONE_TIMESTAMP

  /** Helper to tint a [Drawable], [ColorStateList] or a [View] */
  val tinter by lazy { CyaneaTinter() }
  val themes by lazy { CyaneaThemes(this) }

  @ColorInt internal var backgroundDark: Int
  @ColorInt internal var backgroundDarkLighter: Int
  @ColorInt internal var backgroundDarkDarker: Int
  @ColorInt internal var backgroundLight: Int
  @ColorInt internal var backgroundLightLighter: Int
  @ColorInt internal var backgroundLightDarker: Int

  internal var timestamp: Long
    private set

  init {
    primary = prefs.getInt(PREF_PRIMARY,
        res.getColor(R.color.cyanea_primary_reference))
    primaryDark = prefs.getInt(PREF_PRIMARY_DARK,
        res.getColor(R.color.cyanea_primary_dark_reference))
    primaryLight = prefs.getInt(PREF_PRIMARY_LIGHT,
        res.getColor(R.color.cyanea_primary_light_reference))

    accent = prefs.getInt(PREF_ACCENT,
        res.getColor(R.color.cyanea_accent_reference))
    accentDark = prefs.getInt(PREF_ACCENT_DARK,
        res.getColor(R.color.cyanea_accent_dark_reference))
    accentLight = prefs.getInt(PREF_ACCENT_LIGHT,
        res.getColor(R.color.cyanea_accent_light_reference))

    backgroundLight = prefs.getInt(PREF_BACKGROUND_LIGHT,
        res.getColor(R.color.cyanea_bg_light))
    backgroundLightDarker = prefs.getInt(PREF_BACKGROUND_LIGHT_DARKER,
        res.getColor(R.color.cyanea_bg_light_darker))
    backgroundLightLighter = prefs.getInt(PREF_BACKGROUND_LIGHT_LIGHTER,
        res.getColor(R.color.cyanea_bg_light_lighter))

    backgroundDark = prefs.getInt(PREF_BACKGROUND_DARK,
        res.getColor(R.color.cyanea_bg_dark))
    backgroundDarkDarker = prefs.getInt(PREF_BACKGROUND_DARK_DARKER,
        res.getColor(R.color.cyanea_bg_dark_darker))
    backgroundDarkLighter = prefs.getInt(PREF_BACKGROUND_DARK_LIGHTER,
        res.getColor(R.color.cyanea_bg_dark_lighter))

    baseTheme = getBaseTheme(prefs, res)

    menuIconColor = prefs.getInt(PREF_MENU_ICON_COLOR,
        res.getColor(if (isActionBarLight) R.color.cyanea_menu_icon_dark else R.color.cyanea_menu_icon_light))
    subMenuIconColor = prefs.getInt(PREF_SUB_MENU_ICON_COLOR,
        res.getColor(if (baseTheme == LIGHT) R.color.cyanea_sub_menu_icon_dark else R.color.cyanea_sub_menu_icon_light))

    navigationBar = prefs.getInt(PREF_NAVIGATION_BAR,
        res.getColor(R.color.cyanea_navigation_bar_reference))

    shouldTintStatusBar = prefs.getBoolean(PREF_SHOULD_TINT_STATUS_BAR,
        res.getBoolean(R.bool.should_tint_status_bar))
    shouldTintNavBar = prefs.getBoolean(PREF_SHOULD_TINT_NAV_BAR,
        res.getBoolean(R.bool.should_tint_nav_bar))

    timestamp = prefs.getLong(PREF_TIMESTAMP, NONE_TIMESTAMP)

    setDefaultDarkerAndLighterColors()
  }

  /**
   * Tint all items and sub-menu items in a [menu][Menu]
   *
   * @param menu the Menu to tint
   * @param activity the current Activity
   * @param forceIcons False to hide sub-menu icons from showing. True by default.
   */
  @JvmOverloads
  fun tint(menu: Menu, activity: Activity, forceIcons: Boolean = true) =
      MenuTint(menu,
          menuIconColor = menuIconColor,
          subIconColor = subMenuIconColor,
          forceIcons = forceIcons
      ).apply(activity)

  /**
   * Create a new [Editor] to edit this instance
   */
  fun edit() = Editor(this)

  /**
   * Creates a new editor and applys any edits in the action parameter
   */
  inline fun edit(action: Cyanea.Editor.() -> Unit) = edit().also { editor -> action(editor) }.apply()

  private fun setDefaultDarkerAndLighterColors() {
    // We use a transparent primary|accent dark|light colors so the library user
    // is not required to specify a color value for for accent|primary light|dark
    // If the theme is using the transparent (fake) primary dark color, we need
    // to update our color values and create light|dark variants for them.
    if (primaryDark == getOriginalColor(R.color.cyanea_default_primary_dark)) {
      primaryDark = ColorUtils.darker(primary, DEFAULT_DARKER_FACTOR)
    }
    if (primaryLight == getOriginalColor(R.color.cyanea_default_primary_light)) {
      primaryLight = ColorUtils.lighter(primary, DEFAULT_LIGHTER_FACTOR)
    }
    if (accentDark == getOriginalColor(R.color.cyanea_default_accent_dark)) {
      accentDark = ColorUtils.darker(accent, DEFAULT_DARKER_FACTOR)
    }
    if (accentLight == getOriginalColor(R.color.cyanea_default_accent_light)) {
      accentLight = ColorUtils.lighter(accent, DEFAULT_LIGHTER_FACTOR)
    }
  }

  companion object {

    @SuppressLint("StaticFieldLeak") // application context is safe
    internal lateinit var app: Application
    lateinit var res: Resources

    /**
     * Initialize Cyanea. This should be done in the [application][Application] class.
     */
    @JvmStatic
    fun init(app: Application, res: Resources) {
      this.app = app
      this.res = res
    }

    /**
     * Check if Cyanea has been initialized.
     *
     * @see [init]
     */
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
            val preferences = app.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            return Cyanea(preferences)
          } catch (e: UninitializedPropertyAccessException) {
            throw IllegalStateException("Cyanea.init must be called before referencing the singleton instance")
          }
        }
    }

    private val instances by lazy { mutableMapOf<String, Cyanea>() }

    /**
     * The singleton [Cyanea] instance that you can use throughout the application.
     */
    @JvmStatic
    val instance: Cyanea by lazy { Holder.INSTANCE }

    /**
     * Get a instance of [Cyanea] by name. This will create a new instance if none exist.
     *
     * This allows you to have more than one color scheme in an app. You must override Activity#getCyanea().
     */
    @JvmStatic
    fun getInstance(name: String): Cyanea {
      instances[name]?.let { cyanea ->
        return cyanea
      } ?: run {
        val preferences = app.getSharedPreferences(name, Context.MODE_PRIVATE)
        val cyanea = Cyanea(preferences)
        instances[name] = cyanea
        return cyanea
      }
    }

    /**
     * Turns on logging for the [Cyanea] library
     */
    @JvmStatic
    var loggingEnabled = false

    @JvmStatic
    fun log(tag: String, msg: String, ex: Throwable? = null) {
      if (loggingEnabled) {
        Log.d(tag, msg, ex)
      }
    }

    /**
     * Get the original color of a color resource.
     *
     * @param resid The color resource to retrieve
     */
    @JvmStatic
    @ColorInt
    fun getOriginalColor(@ColorRes resid: Int): Int = res.getColor(resid)

    private fun getBaseTheme(prefs: SharedPreferences, res: Resources): BaseTheme {
      val themeName = prefs.getString(PREF_BASE_THEME, null)
      return when (themeName) {
        LIGHT.name -> LIGHT
        DARK.name -> DARK
        else -> {
          val a = TypedValue()
          app.theme?.resolveAttribute(android.R.attr.windowBackground, a, true)
          return if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            val color = a.data
            val isDarkColor = ColorUtils.isDarkColor(color, LIGHT_ACTIONBAR_LUMINANCE_FACTOR)
            if (isDarkColor) DARK else LIGHT
          } else if (res.getBoolean(R.bool.is_default_theme_light)) {
            LIGHT
          } else {
            DARK
          }
        }
      }
    }

  }

  /**
   * An editor for Cyanea to change colors and other values
   */
  @Suppress("MemberVisibilityCanBePrivate")
  class Editor internal constructor(private val cyanea: Cyanea) {

    private val editor = cyanea.prefs.edit()

    /**
     * Set the [primary] color using a color resource.
     *
     * The [primaryDark], [primaryLight], [navigationBar], and [menuIconColor] will also be updated to match the theme.
     */
    fun primaryResource(@ColorRes resid: Int) = primary(res.getColor(resid))

    /** Set the [primary] dark color using a color resource. */
    fun primaryDarkResource(@ColorRes resid: Int) = primaryDark(res.getColor(resid))

    /** Set the [primary] light color using a color resource. */
    fun primaryLightResource(@ColorRes resid: Int) = primaryLight(res.getColor(resid))

    /**
     * Set the [accent] dark color using a color resource.
     *
     * The [accentDark] and [accentLight] colors will also be updated.
     */
    fun accentResource(@ColorRes resid: Int): Editor = accent(res.getColor(resid))

    /** Set the [accent] dark color using a color resource. */
    fun accentDarkResource(@ColorRes resid: Int) = accentDark(res.getColor(resid))

    /** Set the [accent] light color using a color resource. */
    fun accentLightResource(@ColorRes resid: Int) = accentLight(res.getColor(resid))

    /**
     * Set the background color using a color resource.
     *
     * The [baseTheme], [backgroundLight], [backgroundDark] and [subMenuIconColor] will also be updated.
     */
    fun backgroundResource(@ColorRes resid: Int) = background(res.getColor(resid))

    /** Set the background color for a [LIGHT] theme using a color resource. */
    fun backgroundLightResource(@ColorRes resid: Int) = backgroundLight(res.getColor(resid))

    /** Set the background dark color for a [LIGHT] theme using a color resource. */
    fun backgroundLightDarkerResource(@ColorRes resid: Int) = backgroundLightDarker(res.getColor(resid))

    /** Set the background light color for a [LIGHT] theme using a color resource. */
    fun backgroundLightLighterResource(@ColorRes resid: Int) = backgroundLightLighter(res.getColor(resid))

    /** Set the background color for a [DARK] theme using a color resource. */
    fun backgroundDarkResource(@ColorRes resid: Int) = backgroundDark(res.getColor(resid))

    /** Set the background dark color for a [DARK] theme using a color resource. */
    fun backgroundDarkDarkerResource(@ColorRes resid: Int) = backgroundDarkDarker(res.getColor(resid))

    /** Set the background light color for a [DARK] theme using a color resource. */
    fun backgroundDarkLighterResource(@ColorRes resid: Int) = backgroundDarkLighter(res.getColor(resid))

    /** Set the [menuIconColor] using a color resource */
    fun menuIconColorResource(@ColorRes resid: Int) = menuIconColor(res.getColor(resid))

    /** Set the [subMenuIconColor] using a color resource */
    fun subMenuIconColorResource(@ColorRes resid: Int) = subMenuIconColor(res.getColor(resid))

    /** Set the [navigationBar] color using a color resource */
    fun navigationBarResource(@ColorRes resid: Int) = navigationBar(res.getColor(resid))

    /**
     * Set the [primary] color using a color resource.
     *
     * The [primaryDark], [primaryLight], [navigationBar], and [menuIconColor] will also be updated to match the theme.
     */
    fun primary(@ColorInt color: Int): Editor {
      cyanea.primary = color
      editor.putInt(PREF_PRIMARY, color)
      val isDarkColor = ColorUtils.isDarkColor(color, LIGHT_ACTIONBAR_LUMINANCE_FACTOR)
      val menuIconColorRes = if (isDarkColor) R.color.cyanea_menu_icon_light else R.color.cyanea_menu_icon_dark
      primaryDark(ColorUtils.darker(color, DEFAULT_DARKER_FACTOR))
      primaryLight(ColorUtils.lighter(color, DEFAULT_LIGHTER_FACTOR))
      menuIconColor(res.getColor(menuIconColorRes))
      navigationBar(if (isDarkColor) color else Color.BLACK)
      return this
    }

    /** Set the [primary] dark color using a color resource. */
    fun primaryDark(@ColorInt color: Int): Editor {
      cyanea.primaryDark = color
      editor.putInt(PREF_PRIMARY_DARK, color)
      return this
    }

    /** Set the [primary] light color using a color resource. */
    fun primaryLight(@ColorInt color: Int): Editor {
      cyanea.primaryLight = color
      editor.putInt(PREF_PRIMARY_LIGHT, color)
      return this
    }

    /**
     * Set the [accent] dark color using a color resource.
     *
     * The [accentDark] and [accentLight] colors will also be updated.
     */
    fun accent(@ColorInt color: Int): Editor {
      cyanea.accent = color
      editor.putInt(PREF_ACCENT, color)
      accentDark(ColorUtils.darker(color, DEFAULT_DARKER_FACTOR))
      accentLight(ColorUtils.lighter(color, DEFAULT_LIGHTER_FACTOR))
      return this
    }

    /** Set the [accent] dark color using a color resource. */
    fun accentDark(@ColorInt color: Int): Editor {
      cyanea.accentDark = color
      editor.putInt(PREF_ACCENT_DARK, color)
      return this
    }

    /** Set the [accent] light color using a color resource. */
    fun accentLight(@ColorInt color: Int): Editor {
      cyanea.accentLight = color
      editor.putInt(PREF_ACCENT_LIGHT, color)
      return this
    }

    /**
     * Set the background color using a color resource.
     *
     * The [baseTheme], [backgroundLight], [backgroundDark] and [subMenuIconColor] will also be updated.
     */
    fun background(@ColorInt color: Int): Editor {
      val lighter = ColorUtils.lighter(color, DEFAULT_LIGHTER_FACTOR)
      val darker = ColorUtils.darker(color, DEFAULT_DARKER_FACTOR)
      val isDarkColor = ColorUtils.isDarkColor(color, LIGHT_ACTIONBAR_LUMINANCE_FACTOR)
      if (isDarkColor) {
        baseTheme(DARK)
        backgroundDark(color)
        backgroundDarkDarker(darker)
        backgroundDarkLighter(lighter)
        subMenuIconColor(res.getColor(R.color.cyanea_sub_menu_icon_light))
      } else {
        baseTheme(LIGHT)
        backgroundLight(color)
        backgroundLightDarker(darker)
        backgroundLightLighter(lighter)
        subMenuIconColor(res.getColor(R.color.cyanea_sub_menu_icon_dark))
      }
      return this
    }

    /** Set the background color for a [LIGHT] theme using a literal (hardcoded) color integer. */
    fun backgroundLight(@ColorInt color: Int): Editor {
      cyanea.backgroundLight = color
      editor.putInt(PREF_BACKGROUND_LIGHT, color)
      return this
    }

    /** Set the background dark color for a [LIGHT] theme using a literal (hardcoded) color integer. */
    fun backgroundLightDarker(@ColorInt color: Int): Editor {
      cyanea.backgroundDarkDarker = color
      editor.putInt(PREF_BACKGROUND_LIGHT_DARKER, color)
      return this
    }

    /** Set the background light color for a [LIGHT] theme using a literal (hardcoded) color integer. */
    fun backgroundLightLighter(@ColorInt color: Int): Editor {
      cyanea.backgroundLightLighter = color
      editor.putInt(PREF_BACKGROUND_LIGHT_LIGHTER, color)
      return this
    }

    /** Set the background color for a [DARK] theme using a literal (hardcoded) color integer. */
    fun backgroundDark(@ColorInt color: Int): Editor {
      cyanea.backgroundDark = color
      editor.putInt(PREF_BACKGROUND_DARK, color)
      return this
    }

    /** Set the background dark color for a [DARK] theme using a literal (hardcoded) color integer. */
    fun backgroundDarkDarker(@ColorInt color: Int): Editor {
      cyanea.backgroundDarkDarker = color
      editor.putInt(PREF_BACKGROUND_DARK_DARKER, color)
      return this
    }

    /** Set the background light color for a [DARK] theme using a literal (hardcoded) color integer. */
    fun backgroundDarkLighter(@ColorInt color: Int): Editor {
      cyanea.backgroundDarkLighter = color
      editor.putInt(PREF_BACKGROUND_DARK_LIGHTER, color)
      return this
    }

    /** Set the [menuIconColor] using a literal (hardcoded) color integer */
    fun menuIconColor(@ColorInt color: Int): Editor {
      cyanea.menuIconColor = color
      editor.putInt(PREF_MENU_ICON_COLOR, color)
      return this
    }

    /** Set the [subMenuIconColor] using a literal (hardcoded) color integer */
    fun subMenuIconColor(@ColorInt color: Int): Editor {
      cyanea.subMenuIconColor = color
      editor.putInt(PREF_SUB_MENU_ICON_COLOR, color)
      return this
    }

    /** Set the [navigationBar] color using a literal (hardcoded) color integer */
    fun navigationBar(@ColorInt color: Int): Editor {
      cyanea.navigationBar = color
      editor.putInt(PREF_NAVIGATION_BAR, color)
      return this
    }

    /** Set whether or not to tint the system status bar */
    fun shouldTintStatusBar(choice: Boolean): Editor {
      cyanea.shouldTintStatusBar = choice
      editor.putBoolean(PREF_SHOULD_TINT_STATUS_BAR, choice)
      return this
    }

    /** Set whether or not to tint the system navigation bar */
    fun shouldTintNavBar(choice: Boolean): Editor {
      cyanea.shouldTintNavBar = choice
      editor.putBoolean(PREF_SHOULD_TINT_NAV_BAR, choice)
      return this
    }

    /** Set the base theme. Either [LIGHT] or [DARK]. This should correlate with the [backgroundColor] */
    fun baseTheme(theme: BaseTheme): Editor {
      cyanea.baseTheme = theme
      editor.putString(PREF_BASE_THEME, theme.name)
      return this
    }

    /**
     * Apply preferences to the editor. For theme changes to be applied you must recreate the activity.
     */
    fun apply(): Recreator {
      cyanea.timestamp = System.currentTimeMillis()
      editor.putLong(PREF_TIMESTAMP, cyanea.timestamp)
      editor.apply()
      return Recreator()
    }

  }

  /**
   * Helper to recreate a modified themed activity
   */
  class Recreator {

    /**
     * Recreate the current activity
     *
     * @param activity The current activity
     * @param delay The delay in milliseconds until the activity is recreated
     * @param smooth True to use a fade-in/fade-out animation when re-creating.
     * Use with caution, this will create a new instance of the activity.
     */
    @JvmOverloads
    fun recreate(activity: Activity, delay: Long = DEFAULT_DELAY, smooth: Boolean = false) {
      Handler().postDelayed({
        activity.run {
          if (smooth) {
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
          } else {
            recreate()
          }
        }
      }, delay)
    }

    companion object {
      private const val DEFAULT_DELAY = 200L
    }

  }

  /**
   * Callback when a theme has been modified and the [Activity] has been recreated.
   */
  interface ThemeModifiedListener {

    /**
     * Called in [onResume][Activity.onResume] of an [Activity] when the theme has been modified.
     */
    fun onThemeModified()
  }

  @Keep
  enum class BaseTheme {
    LIGHT,
    DARK
  }

}