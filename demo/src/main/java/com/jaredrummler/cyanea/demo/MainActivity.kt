package com.jaredrummler.cyanea.demo

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.jaredrummler.cyanea.CyaneaTheme
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.utils.ColorUtils
import kotlinx.android.synthetic.main.activity_main.gridView


class MainActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    gridView.adapter = ThemeAdapter(CyaneaTheme.from(assets, "themes/cyanea_themes.json"))
    gridView.setOnItemClickListener { parent, view, position, id ->
      val theme = (gridView.adapter as ThemeAdapter).getItem(position)
      val themeName = theme.themeName
      Toast.makeText(applicationContext, "Clicked $themeName", Toast.LENGTH_LONG).show()
      theme.apply(cyanea)
      recreate()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.add(0, 1, 0, "Menu Item #1")
        .setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

    menu.add(0, 2, 0, "Menu Item #2")
        .setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    menu.add(0, 3, 0, "Menu Item #3")
        .setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    return super.onCreateOptionsMenu(menu)
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

  class ThemeAdapter(private val themes: List<CyaneaTheme>) : BaseAdapter() {

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

  }

  class ViewHolder(val itemView: View) {

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
