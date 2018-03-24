package com.jaredrummler.cyanea.demo

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.jaredrummler.cyanea.CyaneaResources
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

  override fun getResources(): Resources {
    return CyaneaResources(super.getResources())
  }

  class MyDecorator : Decorator {
    override fun apply(view: View, attrs: AttributeSet) {
      if (view is TextView) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.color_primary))
      }
    }

  }

}
