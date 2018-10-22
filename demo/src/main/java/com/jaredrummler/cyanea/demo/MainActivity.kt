package com.jaredrummler.cyanea.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.fragments.AboutFragment
import com.jaredrummler.cyanea.demo.themes.ThemePickerActivity
import kotlinx.android.synthetic.main.activity_main.bar
import kotlinx.android.synthetic.main.activity_main.fab

class MainActivity : CyaneaAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(R.id.container, AboutFragment())
          .commit()
    }

    fab.setOnClickListener {
      startActivity(Intent(this, ThemePickerActivity::class.java))
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
