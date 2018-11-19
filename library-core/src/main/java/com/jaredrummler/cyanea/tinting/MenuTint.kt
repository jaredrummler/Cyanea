package com.jaredrummler.cyanea.tinting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toolbar
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.widget.ActionMenuView
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.utils.Reflection

/**
 * Apply colors and/or transparency to menu icons in a [menu][Menu].
 *
 * Example usage:
 *
 * ```
 * override fun onCreateOptionsMenu(Menu menu): Boolean {
 *     ...
 *     val color = resources.getColor(R.color.your_awesome_color)
 *     val alpha = 204
 *     MenuTint(menu, menuIconColor = color, menuIconAlpha = alpha).apply(this)
 *     ...
 * }
 * ```
 *
 * @param menu The menu to tint
 * @param menuIconColor The color to apply on visible MenuItem icons, including the OverflowMenuButton.
 * @param menuIconAlpha The alpha value for the drawable. 0 means fully transparent, and 255 means fully opaque.
 * @param subIconColor The color to apply on visible MenuItem icons, including the OverflowMenuButton.
 * @param subIconAlpha The alpha value for the drawable. 0 means fully transparent, and 255 means fully opaque.
 * @param overflowDrawableRes The drawable id to set on the OverflowMenuButton.
 * @param reApplyOnChange `true` to re-apply tinting when an action view is expanded or collapsed.
 * @param forceIcons `true` to set the menu to show MenuItem icons in the overflow window.
 * @param tintOverflowIcon `true` to tint the OverflowMenuButton. True by default.
 * @param tintNavigationIcon `true` to tint the navigation icon. True by default.
 */
class MenuTint(
    val menu: Menu,
    @ColorInt private var menuIconColor: Int? = null,
    private var menuIconAlpha: Int? = null,
    @ColorInt private val subIconColor: Int? = null,
    private val subIconAlpha: Int? = null,
    @DrawableRes private val overflowDrawableRes: Int? = null,
    @ColorInt private val originalMenuIconColor: Int? = null,
    private val reApplyOnChange: Boolean = false,
    private val forceIcons: Boolean = false,
    private val tintOverflowIcon: Boolean = true,
    private val tintNavigationIcon: Boolean = true
) {

  private var actionBar: ViewGroup? = null

  /**
   * Sets a [android.graphics.ColorFilter] and/or alpha on all the [MenuItem]s in the menu,
   * including the OverflowMenuButton.
   *
   * Call this method after inflating/creating your menu in [Activity.onCreateOptionsMenu]
   *
   * @param context The [activity][Activity] context
   */
  fun apply(context: Context) {
    if (forceIcons) {
      forceMenuIcons(menu)
    }

    val size = menu.size()
    for (i in 0 until size) {
      val item = menu.getItem(i)
      colorMenuItem(item, menuIconColor, menuIconAlpha)
      colorSubMenus(item, subIconColor, subIconAlpha)
      if (reApplyOnChange) {
        item.actionView?.let {
          item.setOnActionExpandListener(ActionExpandListener())
        }
      }
    }

    if (context is Activity) {
      // Tint the items on the Toolbar after the Toolbar is created
      actionBar = findActionBar(context)
      actionBar?.let { actionBar ->
        actionBar.post {
          for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (isInOverflow(menuItem)) {
              colorMenuItem(menuItem, subIconColor, subIconAlpha)
            }
            colorSubMenus(menuItem, subIconColor, subIconAlpha)
          }

          // Tint the navigation icon
          if (tintNavigationIcon) {
            tintActionBar(actionBar)
          }

          // Tint the overflow icon
          if (tintOverflowIcon) {
            tintOverflow()
          }
        }
      }
    }
  }

  private fun reapply() {
    val size = menu.size()
    for (i in 0 until size) {
      val item = menu.getItem(i)
      if (isActionButton(item)) {
        colorMenuItem(item, menuIconColor, menuIconAlpha)
      }
    }

    actionBar?.post {
      for (i in 0 until size) {
        val item = menu.getItem(i)
        if (isInOverflow(item)) {
          colorMenuItem(item, subIconColor, subIconAlpha)
        } else {
          colorMenuItem(item, menuIconColor, menuIconAlpha)
        }
        colorSubMenus(item, subIconColor, subIconAlpha)

        // Tint the overflow icon
        if (tintOverflowIcon) {
          tintOverflow()
        }
      }
    }
  }

  private fun tintActionBar(actionBar: ViewGroup) {
    if (actionBar is androidx.appcompat.widget.Toolbar) {
      actionBar.navigationIcon?.let { icon ->
        menuIconColor?.let { color ->
          val navigationIcon = icon.mutate()
          navigationIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
      }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && actionBar is Toolbar) {
      actionBar.navigationIcon?.let { icon ->
        menuIconColor?.let { color ->
          val navigationIcon = icon.mutate()
          navigationIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
      }
    }
  }

  private fun tintOverflow() {
    findOverflowMenuButton(actionBar)?.let { overflowView ->
      overflowDrawableRes?.let { resId -> overflowView.setImageResource(resId) }
      menuIconColor?.let { color -> overflowView.setColorFilter(color) }
      menuIconAlpha?.let { alpha ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          overflowView.imageAlpha = alpha
        } else {
          @Suppress("DEPRECATION")
          overflowView.setAlpha(alpha)
        }
      }
    }
  }

  companion object {

    private const val TAG = "MenuTint"

    /**
     * Sets the color filter and/or the alpha transparency on a [MenuItem]'s icon.
     *
     * @param menuItem The [MenuItem] to theme.
     * @param color The color to set for the color filter or `null` for no changes.
     * @param alpha The alpha value (0...255) to set on the icon or `null` for no changes.
     */
    fun colorMenuItem(menuItem: MenuItem, color: Int?, alpha: Int? = null) {
      menuItem.icon?.let { icon ->
        val drawable = icon.mutate()
        color?.let { drawable.setColorFilter(it, PorterDuff.Mode.SRC_IN) }
        alpha?.let { drawable.alpha = it }
        menuItem.icon = drawable
      }
    }

    /**
     * Sets the color filter and/or the alpha transparency on a [MenuItem]'s sub menus
     *
     * @param item The [MenuItem] to theme.
     * @param color The color to set for the color filter or `null` for no changes.
     * @param alpha The alpha value (0...255) to set on the icon or `null` for no changes.
     */
    fun colorSubMenus(item: MenuItem, color: Int?, alpha: Int? = null) {
      if (item.hasSubMenu()) {
        item.subMenu?.let { menu ->
          val size = menu.size()
          for (i in 0 until size) {
            val menuItem = menu.getItem(i)
            colorMenuItem(menuItem, color, alpha)
            colorSubMenus(menuItem, color, alpha)
          }
        }
      }
    }

    /**
     * Check if an item is showing (not in the overflow menu).
     *
     * @param item The MenuItem.
     * @return `true` if the MenuItem is visible on the ActionBar.
     */
    @SuppressLint("RestrictedApi")
    fun isActionButton(item: MenuItem): Boolean {
      if (item is MenuItemImpl) {
        return item.isActionButton
      }
      return Reflection.invoke(item, "isActionButton") ?: false
    }

    /**
     * Check if an item is in the overflow menu.
     *
     * @param item The MenuItem
     * @return `true` if the MenuItem is in the overflow menu.
     * @see [MenuTint.isActionButton]
     */
    fun isInOverflow(item: MenuItem) = !isActionButton(item)

    /**
     * Set the menu to show MenuItem icons in the overflow window.
     *
     * @param menu The menu to force icons to show
     */
    fun forceMenuIcons(menu: Menu) {
      Reflection.invoke<Any?>(menu, "setOptionalIconsVisible", arrayOf(Boolean::class.javaPrimitiveType!!), true)
    }

    /**
     * Force the app to show the overflow menu. The menu button will still work, but it will open the menu in the
     * top right corner.
     *
     * See: http://stackoverflow.com/a/11438245/1048340
     *
     * @param context The current context
     */
    fun forceOverflow(context: Context) {
      val configuration = ViewConfiguration.get(context)
      Reflection.getField(configuration, "sHasPermanentMenuKey")?.set(configuration, false)
    }

    private fun findOverflowMenuButton(viewGroup: ViewGroup?): ImageView? {
      if (viewGroup == null) return null
      var i = 0
      val count = viewGroup.childCount
      while (i < count) {
        val view = viewGroup.getChildAt(i)
        if (view is ImageView &&
            (view.javaClass.simpleName == "OverflowMenuButton"
                || view is ActionMenuView.ActionMenuChildView)) {
          return view
        } else if (view is ViewGroup) {
          findOverflowMenuButton(view)?.let { btn -> return btn }
        }
        i++
      }
      return null
    }

    private fun findActionBar(activity: Activity): ViewGroup? {
      try {
        val id = activity.resources.getIdentifier("action_bar", "id", "android")
        if (id != 0) {
          activity.findViewById<ViewGroup>(id)?.let { return it }
        }
        return findToolbar(activity.findViewById<View>(android.R.id.content).rootView as ViewGroup)
      } catch (e: Exception) {
        Cyanea.log(TAG, "Error finding ActionBar", e)
      }
      return null
    }

    private fun findToolbar(viewGroup: ViewGroup): ViewGroup? {
      var toolbar: ViewGroup? = null
      var i = 0
      val count = viewGroup.childCount
      while (i < count) {
        val view = viewGroup.getChildAt(i)
        if (view.javaClass == androidx.appcompat.widget.Toolbar::class.java
            || view.javaClass.name == "android.widget.Toolbar") {
          toolbar = view as ViewGroup
        } else if (view is ViewGroup) {
          toolbar = findToolbar(view)
        }
        if (toolbar != null) {
          break
        }
        i++
      }
      return toolbar
    }

  }

  inner class ActionExpandListener : MenuItem.OnActionExpandListener {
    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
      val color = originalMenuIconColor ?: menuIconColor
      menuIconColor = color
      reapply()
      return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
      val color = originalMenuIconColor ?: menuIconColor
      menuIconColor = color
      reapply()
      return true
    }

  }


}