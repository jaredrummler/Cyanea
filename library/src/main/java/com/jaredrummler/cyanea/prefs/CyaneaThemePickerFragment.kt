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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.app.CyaneaFragment

/**
 * Fragment containing the theme picker
 */
open class CyaneaThemePickerFragment : CyaneaFragment(), OnItemClickListener {

  open val themesJsonAssetPath get() = "themes/cyanea_themes.json"

  private lateinit var gridView: GridView

  private var disableClick = false

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
    if(disableClick) return
    val theme = (gridView.adapter as CyaneaThemePickerAdapter).getItem(position)
    val themeName = theme.themeName
    Cyanea.log(TAG, "Clicked $themeName")
    theme.apply(cyanea).recreate(requireActivity(), smooth = true)
    disableClick = true
    view?.postDelayed({
      disableClick = false
    }, DELAY_CLICK_TIME)
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
    private const val DELAY_CLICK_TIME = 2000L
    fun newInstance() = CyaneaThemePickerFragment()
  }

}