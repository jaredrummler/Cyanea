package com.jaredrummler.cyanea.inflator.processors

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.Switch
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R

@TargetApi(Build.VERSION_CODES.M)
class SwitchProcessor : CyaneaViewProcessor<Switch>() {

  @SuppressLint("PrivateResource")
  override fun process(view: Switch, attrs: AttributeSet?, cyanea: Cyanea) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      cyanea.tinter.tint(view.thumbDrawable)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      view.trackTintList = ContextCompat.getColorStateList(view.context, R.color.abc_tint_switch_track)
    }
  }

  override fun getType(): Class<Switch> = Switch::class.java

}