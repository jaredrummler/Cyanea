package com.jaredrummler.cyanea.demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.jaredrummler.cyanea.app.CyaneaFragment
import com.jaredrummler.cyanea.demo.R
import kotlinx.android.synthetic.main.fragment_main.cyaneaTopics

class MainFragment : CyaneaFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initCyaneaTopics()
  }

  private fun initCyaneaTopics() {
    cyaneaTopics.removeAllViews()
    val textArray = resources.getStringArray(R.array.cyanea_topics)

    for (text in textArray) {
      val chip = layoutInflater.inflate(R.layout.cat_chip_topic, cyaneaTopics, false) as Chip
      @Suppress("DEPRECATION")
      chip.chipText = text
//      chip.text = text
      cyaneaTopics.addView(chip)
    }

  }

}