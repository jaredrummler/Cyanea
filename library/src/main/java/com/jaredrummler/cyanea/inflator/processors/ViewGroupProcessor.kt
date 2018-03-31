package com.jaredrummler.cyanea.inflator.processors

import android.annotation.TargetApi
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.AbsListView
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.tinting.EdgeEffectTint
import com.jaredrummler.cyanea.tinting.WidgetTint

@TargetApi(Build.VERSION_CODES.M)
class ViewGroupProcessor : CyaneaViewProcessor<ViewGroup>() {

  override fun process(view: ViewGroup, attrs: AttributeSet?, cyanea: Cyanea) {
    EdgeEffectTint.setEdgeGlowColor(view, cyanea.primary)
    cyanea.tinter.tint(view.background)
    if (view is AbsListView) {
      WidgetTint.setFastScrollThumbColor(view, cyanea.accent)
    }
  }

  override fun getType(): Class<ViewGroup> = ViewGroup::class.java

}