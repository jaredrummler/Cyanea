package com.jaredrummler.cyanea.inflator.processors

import android.annotation.TargetApi
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageButton
import com.jaredrummler.cyanea.Cyanea

@TargetApi(Build.VERSION_CODES.M)
class ImageButtonProcessor : CyaneaViewProcessor<ImageButton>() {

  override fun getType(): Class<ImageButton> = ImageButton::class.java

  override fun process(view: ImageButton, attrs: AttributeSet?, cyanea: Cyanea) {
    cyanea.tinter.tint(view.background)
  }

}