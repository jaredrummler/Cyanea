package com.jaredrummler.cyanea.inflator.processors

import android.R.attr
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.CompoundButton
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.utils.ColorUtils

/**
 * A [CyaneaViewProcessor] that styles [buttons][CompoundButton] in the overflow menu.
 */
@RequiresApi(Build.VERSION_CODES.M)
class CompoundButtonProcessor : CyaneaViewProcessor<CompoundButton>() {

  override fun getType(): Class<CompoundButton> = CompoundButton::class.java

  @SuppressLint("PrivateResource")
  override fun process(view: CompoundButton, attrs: AttributeSet?, cyanea: Cyanea) {
    view.buttonTintList?.let { cyanea.tinter.tint(it) } ?: run {
      view.buttonTintList = cyanea.tinter.tint(
          view.context.getColorStateList(R.color.abc_tint_btn_checkable)
      )
    }
    val background = view.background
    if (background is RippleDrawable) {
      val resid = if (cyanea.isDark) R.color.ripple_material_dark else R.color.ripple_material_light
      val unchecked = ContextCompat.getColor(view.context, resid)
      val checked = ColorUtils.adjustAlpha(cyanea.accent, 0.4f)
      val csl = ColorStateList(
          arrayOf(
              intArrayOf(-attr.state_activated, -attr.state_checked),
              intArrayOf(attr.state_activated),
              intArrayOf(attr.state_checked)
          ),
          intArrayOf(unchecked, checked, checked)
      )
      background.setColor(csl)
    }
  }

}