package com.jaredrummler.cyanea.inflator.processors

import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import com.jaredrummler.cyanea.Cyanea

class TextViewProcessor : CyaneaViewProcessor<TextView>() {

  override fun process(view: TextView, attrs: AttributeSet?, cyanea: Cyanea) {
    view.textColors?.let { colors ->
      view.setTextColor(cyanea.tinter.tint(colors))
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      cyanea.tinter.tint(view.backgroundTintList)
    }
    cyanea.tinter.tint(view.background)
  }

  override fun getType(): Class<TextView> = TextView::class.java

}