package com.jaredrummler.cyanea.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.fragments.AboutFragment
import com.jaredrummler.cyanea.demo.fragments.WidgetsFragment
import com.jaredrummler.cyanea.demo.themes.ThemePickerActivity
import kotlinx.android.synthetic.main.activity_main.bar
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.tabLayout
import kotlinx.android.synthetic.main.activity_main.viewPager

class MainActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fab.setOnClickListener {
      startActivity(Intent(this, ThemePickerActivity::class.java))
    }

    viewPager.adapter = MyPagerAdapter(this)
    tabLayout.setupWithViewPager(viewPager)

    if (tabLayout.tabCount > 0) {
      val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(0)
      val p = tab.layoutParams as ViewGroup.MarginLayoutParams
      p.setMargins(resources.getDimension(R.dimen.default_margin).toInt(), 0, 0, 0)
      tab.requestLayout()
    }

    bar.replaceMenu(R.menu.bottom_bar_menu)
    cyanea.tint(bar.menu, this)
    bar.setOnMenuItemClickListener { item ->
      when (item.itemId) {
        R.id.action_share -> {
          Toast.makeText(applicationContext, "TODO: implement", Toast.LENGTH_LONG).show()
        }
        R.id.action_github -> {
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jaredrummler/Cyanea")))
        }
        else -> return@setOnMenuItemClickListener false
      }
      return@setOnMenuItemClickListener true
    }
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}

class MyPagerAdapter(private val activity: FragmentActivity)
  : FragmentStatePagerAdapter(activity.supportFragmentManager) {

  private val items = activity.resources.getStringArray(R.array.tabs)

  override fun getItem(position: Int): Fragment {
    return when (items[position]) {
      activity.getString(R.string.tab_about) -> AboutFragment()
      activity.getString(R.string.tab_widgets) -> WidgetsFragment()
      activity.getString(R.string.tab_dialogs) -> DummyFragment()
      else -> throw IllegalArgumentException("No fragment associated with tab '${items[position]}'")
    }
  }

  override fun getPageTitle(position: Int): CharSequence? = items[position]

  override fun getCount(): Int = items.size

}

class DummyFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return FrameLayout(requireActivity())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.setBackgroundColor(Cyanea.instance.backgroundColor)
  }


}