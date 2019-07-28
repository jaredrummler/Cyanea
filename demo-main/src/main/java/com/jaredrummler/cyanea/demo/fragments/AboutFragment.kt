package com.jaredrummler.cyanea.demo.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.jaredrummler.cyanea.app.CyaneaFragment
import com.jaredrummler.cyanea.demo.R
import kotlinx.android.synthetic.main.fragment_about.cyaneaTopics

class AboutFragment : CyaneaFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_about, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    loadTopics()
  }

  private fun loadTopics() {
    cyaneaTopics.removeAllViews()
    resources.getStringArray(R.array.cyanea_topics).forEach { text ->
      val chip = layoutInflater.inflate(R.layout.cat_chip_topic, cyaneaTopics, false) as Chip
      chip.chipBackgroundColor = ColorStateList.valueOf(
          if (cyanea.isDark) cyanea.backgroundColorLight else cyanea.backgroundColorDark
      )
      chip.setTextColor(if (cyanea.isDark) Color.WHITE else Color.BLACK)
      chip.text = text
      cyaneaTopics.addView(chip)
    }
  }
}
