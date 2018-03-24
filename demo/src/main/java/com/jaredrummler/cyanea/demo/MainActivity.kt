package com.jaredrummler.cyanea.demo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.jaredrummler.cyanea.inflator.CyaneaContextWrapper
import com.jaredrummler.cyanea.inflator.decor.Decorator
import com.jaredrummler.cyanea.inflator.decor.FontDecorator

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(CyaneaContextWrapper(newBase,
        decorators = arrayOf(MyDecorator(), FontDecorator())))
  }

  class MyDecorator : Decorator {
    override fun apply(view: View, attrs: AttributeSet) {
      if (view is TextView) {
        view.setTextColor(Color.parseColor("#0099cc"))
      }
    }

  }

}
