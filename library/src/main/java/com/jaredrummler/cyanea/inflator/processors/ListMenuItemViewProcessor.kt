package com.jaredrummler.cyanea.inflator.processors

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.view.menu.ListMenuItemView
import android.util.AttributeSet
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.utils.Reflection

/**
 * Style menu items
 */
class ListMenuItemViewProcessor : CyaneaViewProcessor<ListMenuItemView>() {

  override fun getType(): Class<ListMenuItemView> = ListMenuItemView::class.java

  override fun process(view: ListMenuItemView, attrs: AttributeSet?, cyanea: Cyanea) {
    Reflection.getFieldValue<Drawable?>(view, "mBackground")?.let { background ->
      Reflection.getFieldValue<Any?>(background, "mStateListState")?.let { stateListState ->
        Reflection.getFieldValue<Array<Drawable>?>(stateListState, "mDrawables")?.let { drawables ->
          if (drawables.isNotEmpty()) {
            drawables[0] = ColorDrawable(cyanea.accent)
          }
        }
      }
    }
  }

}