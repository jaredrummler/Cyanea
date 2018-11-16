package com.jaredrummler.cyanea.demo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.jaredrummler.cyanea.demo.DrawerActivity
import com.jaredrummler.cyanea.demo.R

class OtherFragment : Fragment() {

  private lateinit var container: ViewGroup

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_other, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    container = view.findViewById(R.id.container)

    addButton(R.string.drawer_layout, {
      startActivity(Intent(requireActivity(), DrawerActivity::class.java))
    })
  }

  private fun addButton(@StringRes stringResId: Int, action: () -> Unit,
      marginTop: Float = 8f, marginBottom: Float = 0f) {
    val dialogLauncherButton = MaterialButton(requireActivity())
    dialogLauncherButton.setOnClickListener { action() }
    dialogLauncherButton.setText(stringResId)
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    params.topMargin = Math.round(marginTop * resources.displayMetrics.density)
    params.bottomMargin = Math.round(marginBottom * resources.displayMetrics.density)
    container.addView(dialogLauncherButton, params)
  }

}