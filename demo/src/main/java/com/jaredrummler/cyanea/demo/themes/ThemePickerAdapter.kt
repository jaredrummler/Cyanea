package com.jaredrummler.cyanea.demo.themes

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jaredrummler.cyanea.CyaneaTheme
import com.jaredrummler.cyanea.demo.R
import com.jaredrummler.cyanea.utils.ColorUtils

class ThemePickerAdapter(private val themes: List<CyaneaTheme>) : BaseAdapter() {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val holder: ViewHolder

    holder = if (convertView == null) {
      val inflater = LayoutInflater.from(parent.context)
      val view = inflater.inflate(R.layout.item_cyanea_theme, parent, false)
      ViewHolder(view)
    } else {
      convertView.tag as ViewHolder
    }

    val theme = themes[position]

    holder.find<FrameLayout>(R.id.preview).setBackgroundColor(theme.background)
    holder.find<LinearLayout>(R.id.action_bar_panel).setBackgroundColor(theme.primary)

    val fab = holder.find<FloatingActionButton>(R.id.fab_add)
    fab.backgroundTintList = ColorStateList.valueOf(theme.accent)
    fab.rippleColor = theme.accentDark

    val title = holder.find<TextView>(R.id.title)
    title.text = theme.themeName

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

    init {
      itemView.tag = this
    }

    private val views = SparseArray<View>()

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