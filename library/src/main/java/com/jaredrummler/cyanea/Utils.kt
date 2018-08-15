package com.jaredrummler.cyanea

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES

internal object Utils {

  fun atLeastPie() = SDK_INT >= 28
  fun atLeastNougat() = SDK_INT >= VERSION_CODES.N
  fun atLeastMarshmallow() = SDK_INT >= Build.VERSION_CODES.M
  fun atLeastLollipop() = SDK_INT >= Build.VERSION_CODES.LOLLIPOP
  fun atLeastKitKat() = SDK_INT >= Build.VERSION_CODES.KITKAT

}