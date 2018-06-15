package com.jaredrummler.cyanea.demo.themes

import android.os.Bundle
import android.util.Log
import com.jaredrummler.cyanea.CyaneaTheme
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.R
import kotlinx.android.synthetic.main.activity_theme_picker.gridView

class ThemePickerActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_theme_picker)
    gridView.adapter = ThemePickerAdapter(CyaneaTheme.from(assets, "themes/cyanea_themes.json"))
    gridView.setOnItemClickListener { _, _, position, _ ->
      val theme = (gridView.adapter as ThemePickerAdapter).getItem(position)
      val themeName = theme.themeName
      Log.d(TAG, "Clicked $themeName")
      theme.apply(cyanea)
      recreate()
    }
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

  companion object {
    private const val TAG = "ThemePickerActivity"
  }

}