package com.jaredrummler.cyanea.inflator.decor

import android.content.res.AssetManager
import android.content.res.TypedArray
import android.graphics.Typeface
import android.widget.TextView
import com.jaredrummler.cyanea.R

/**
 * Set a font from assets by adding an attribute to the view in your layout XML.
 *
 * ```xml
 * <TextView
 *     ...
 *     app:cyaneaFont="fonts/CustomFont.ttf"
 *     tools:ignore="MissingPrefix" />
 * ```
 */
class FontDecorator : AttrsDecorator<TextView>() {

  private val cache = mutableMapOf<String, Typeface>()

  override fun getType(): Class<TextView> = TextView::class.java

  override fun styleable(): IntArray = R.styleable.FontDecorator

  override fun apply(view: TextView, typedArray: TypedArray) {
    typedArray.getString(R.styleable.FontDecorator_cyaneaFont)?.let { path ->
      view.typeface = getFont(view.context.assets, path)
    }
  }

  private fun getFont(assets: AssetManager, path: String): Typeface? {
    return cache[path] ?: run {
      try {
        Typeface.createFromAsset(assets, path)?.let { font ->
          cache[path] = font
          font
        }
      } catch (e: Exception) {
        null
      }
    }
  }

}