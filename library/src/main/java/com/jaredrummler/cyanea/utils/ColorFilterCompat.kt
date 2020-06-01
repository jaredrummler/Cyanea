package com.jaredrummler.cyanea.utils

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi

/** Get a [ColorFilter] using [BlendMode] on API 29+ and [PorterDuff.Mode] on older APIs */
internal enum class ColorFilterCompat {
  CLEAR, SRC, DST, SRC_OVER, DST_OVER, SRC_IN, DST_IN, SRC_OUT, DST_OUT,
  SRC_ATOP, DST_ATOP, XOR, DARKEN, LIGHTEN, MULTIPLY, SCREEN, ADD, OVERLAY;

  private val porterDuffMode: PorterDuff.Mode
    get() = when (this) {
      CLEAR -> PorterDuff.Mode.CLEAR
      SRC -> PorterDuff.Mode.SRC
      DST -> PorterDuff.Mode.DST
      SRC_OVER -> PorterDuff.Mode.SRC_OVER
      DST_OVER -> PorterDuff.Mode.DST_OVER
      SRC_IN -> PorterDuff.Mode.SRC_IN
      DST_IN -> PorterDuff.Mode.DST_IN
      SRC_OUT -> PorterDuff.Mode.SRC_OUT
      DST_OUT -> PorterDuff.Mode.DST_OUT
      SRC_ATOP -> PorterDuff.Mode.SRC_ATOP
      DST_ATOP -> PorterDuff.Mode.DST_ATOP
      XOR -> PorterDuff.Mode.XOR
      DARKEN -> PorterDuff.Mode.DARKEN
      LIGHTEN -> PorterDuff.Mode.LIGHTEN
      MULTIPLY -> PorterDuff.Mode.MULTIPLY
      SCREEN -> PorterDuff.Mode.SCREEN
      ADD -> PorterDuff.Mode.ADD
      OVERLAY -> PorterDuff.Mode.OVERLAY
    }

  @get:RequiresApi(Build.VERSION_CODES.Q)
  private val blendMode: BlendMode
    get() = when (this) {
      CLEAR -> BlendMode.CLEAR
      SRC -> BlendMode.SRC
      DST -> BlendMode.DST
      SRC_OVER -> BlendMode.SRC_OVER
      DST_OVER -> BlendMode.DST_OVER
      SRC_IN -> BlendMode.SRC_IN
      DST_IN -> BlendMode.DST_IN
      SRC_OUT -> BlendMode.SRC_OUT
      DST_OUT -> BlendMode.DST_OUT
      SRC_ATOP -> BlendMode.SRC_ATOP
      DST_ATOP -> BlendMode.DST_ATOP
      XOR -> BlendMode.XOR
      DARKEN -> BlendMode.DARKEN
      LIGHTEN -> BlendMode.LIGHTEN
      MULTIPLY -> BlendMode.MULTIPLY
      SCREEN -> BlendMode.SCREEN
      ADD -> BlendMode.PLUS
      OVERLAY -> BlendMode.OVERLAY
    }

  fun get(@ColorInt color: Int): ColorFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    BlendModeColorFilter(color, blendMode)
  } else {
    PorterDuffColorFilter(color, porterDuffMode)
  }
}
