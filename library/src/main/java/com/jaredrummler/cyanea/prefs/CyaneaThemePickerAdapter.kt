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

package com.jaredrummler.cyanea.prefs

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.utils.ColorUtils

/**
 * Adapter for items in the theme picker
 */
class CyaneaThemePickerAdapter(private val themes: List<CyaneaTheme>, private val cyanea: Cyanea) : BaseAdapter() {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val holder: ViewHolder

    holder = if (convertView == null) {
      val inflater = LayoutInflater.from(parent.context)
      val view = inflater.inflate(R.layout.cyanea_item_theme, parent, false)
      ViewHolder(view)
    } else {
      convertView.tag as ViewHolder
    }

    val theme = themes[position]

    holder.find<FrameLayout>(R.id.preview).setBackgroundColor(theme.background)
    holder.find<LinearLayout>(R.id.action_bar_panel).setBackgroundColor(theme.primary)

    val fab = holder.find<FloatingActionButton>(R.id.fab_add)
    // Bug: when using the accent color, the last selected item's FAB is the same color as the newly selected FAB.
    // For now, slightly adjust the color.
    val fabMainColor = ColorUtils.lighter(theme.accent, 0.01f)
    fab.backgroundTintList = ColorStateList.valueOf(fabMainColor)
    fab.supportBackgroundTintList = ColorStateList.valueOf(fabMainColor)
    fab.rippleColor = theme.accentDark

    val title = holder.find<TextView>(R.id.title)
    title.text = theme.themeName

    if (theme.isMatchingColorScheme(cyanea)) {
      title.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.cyanea_theme_selected_color))
      title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cyanea_check, 0)
    } else {
      title.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.cyanea_theme_title_bg_color))
      title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    val isDark = ColorUtils.isDarkColor(theme.primary, 0.75)
    val menuIconColor = if (isDark) Color.WHITE else Color.BLACK
    val drawable = DrawerArrowDrawable(parent.context)
    drawable.color = menuIconColor

    val drawer = holder.find<ImageView>(R.id.material_drawer_drawable)
    val overflow = holder.find<ImageView>(R.id.action_overflow)
    drawer.setImageDrawable(drawable)
    overflow.setColorFilter(menuIconColor, PorterDuff.Mode.SRC_ATOP)

    return holder.itemView
  }

  override fun getItem(position: Int): CyaneaTheme = themes[position]

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getCount(): Int = themes.size

  private class ViewHolder(val itemView: View) {

    private val views = SparseArray<View>()

    init {
      itemView.tag = this
    }

    fun <T : View> find(@IdRes id: Int): T {
      views[id]?.let {
        @Suppress("UNCHECKED_CAST")
        return it as T
      } ?: run {
        val view = itemView.findViewById<T>(id)
        views.put(id, view)
        return view
      }
    }
  }
}
