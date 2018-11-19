package com.jaredrummler.cyanea.prefs

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import com.jaredrummler.cyanea.app.CyaneaFragment

open class CyaneaThemePickerFragment : CyaneaFragment(), OnItemClickListener {

  open val themesJsonAssetPath get() = "themes/cyanea_themes.json"

  private lateinit var gridView: GridView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.cyanea_theme_picker, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    gridView = view.findViewById(R.id.gridView)
    val themes = CyaneaTheme.from(requireActivity().assets, themesJsonAssetPath)
    gridView.adapter = CyaneaThemePickerAdapter(themes, cyanea)
    gridView.onItemClickListener = this
    scrollToCurrentTheme(themes)
  }

  override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    val theme = (gridView.adapter as CyaneaThemePickerAdapter).getItem(position)
    val themeName = theme.themeName
    Log.d(TAG, "Clicked $themeName")
    theme.apply(cyanea)
    recreateActivity()
  }

  open fun recreateActivity(delay: Long = RECREATE_DELAY) {
    Handler().postDelayed({
      activity?.run {
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
      }
    }, delay)
  }

  private fun scrollToCurrentTheme(themes: List<CyaneaTheme>) {
    var selectedTheme = -1
    run {
      themes.forEachIndexed { index, theme ->
        if (theme.isMatchingColorScheme(cyanea)) {
          selectedTheme = index
          return@run
        }
      }
    }
    if (selectedTheme != -1) {
      gridView.post {
        if (selectedTheme < gridView.firstVisiblePosition || selectedTheme > gridView.lastVisiblePosition) {
          gridView.setSelection(selectedTheme)
        }
      }
    }
  }

  companion object {
    private const val TAG = "ThemePickerFragment"
    private const val RECREATE_DELAY = 200L
  }

}