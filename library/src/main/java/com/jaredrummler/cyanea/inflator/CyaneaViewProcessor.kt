package com.jaredrummler.cyanea.inflator

import android.R.attr
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.CheckedTextView
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.ListMenuItemView
import androidx.appcompat.widget.AlertDialogLayout
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.textfield.TextInputLayout
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.R
import com.jaredrummler.cyanea.delegate.CyaneaDelegate
import com.jaredrummler.cyanea.tinting.EdgeEffectTint
import com.jaredrummler.cyanea.tinting.WidgetTint
import com.jaredrummler.cyanea.utils.ColorUtils
import com.jaredrummler.cyanea.utils.Reflection

abstract class CyaneaViewProcessor<T : View> {

  /**
   * Process a newly created view.
   *
   * @param view
   * The newly created view.
   * @param attrs
   * The view's [attributes][AttributeSet]
   * @param cyanea
   * The [cyanea][Cyanea] instance used for styling views.
   */
  abstract fun process(view: T, attrs: AttributeSet?, cyanea: Cyanea)

  /**
   * Check if a view should be processed. By default, this checks if the view is an instance of [.getType].
   *
   * @param view
   * The view to check
   * @return [true] if this view should be processed.
   */
  open fun shouldProcessView(view: View): Boolean {
    return getType().isInstance(view)
  }

  /**
   * The class for the given view
   *
   * @return The class for T
   */
  protected abstract fun getType(): Class<T>

  /**
   * An interface that may be used in an [activity][Activity] to provide [view processors][CyaneaViewProcessor]
   * to the [CyaneaDelegate].
   */
  interface Provider {

    /**
     * Get an array of [view processors][CyaneaViewProcessor] to style views.
     *
     * @return An array of decorators for the [CyaneaDelegate].
     */
    fun getViewProcessors(): Array<CyaneaViewProcessor<out View>>

  }

}

// ================================================================================================
// Processors

/**
 * A [CyaneaViewProcessor] that themes alert dialog backgrounds.
 */
internal class AlertDialogProcessor : CyaneaViewProcessor<View>() {

  override fun getType(): Class<View> = View::class.java

  override fun shouldProcessView(view: View): Boolean = view is AlertDialogLayout || CLASS_NAME == view.javaClass.name

  override fun process(view: View, attrs: AttributeSet?, cyanea: Cyanea) {
    view.setBackgroundColor(cyanea.backgroundColor)
  }

  companion object {
    private const val CLASS_NAME = "com.android.internal.widget.AlertDialogLayout"
  }

}

internal class BottomAppBarProcessor : CyaneaViewProcessor<BottomAppBar>() {

  override fun process(view: BottomAppBar, attrs: AttributeSet?, cyanea: Cyanea) {
    view.backgroundTint?.let { view.backgroundTint = cyanea.tinter.tint(it) }
  }

  override fun getType(): Class<BottomAppBar> = BottomAppBar::class.java

}

internal class CheckedTextViewProcessor : CyaneaViewProcessor<CheckedTextView>() {

  override fun process(view: CheckedTextView, attrs: AttributeSet?, cyanea: Cyanea) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      view.compoundDrawableTintList = checkableColorStateList(
          view.context,
          cyanea.accent,
          cyanea.isDark
      )
    } else {
      view.compoundDrawables.forEach { drawable ->
        cyanea.tinter.tint(drawable)
      }
    }
  }

  override fun getType(): Class<CheckedTextView> = CheckedTextView::class.java

  private fun checkableColorStateList(
      context: Context,
      @ColorInt tint: Int,
      isDark: Boolean
  ): ColorStateList {
    val disabled = ContextCompat.getColor(context,
        if (isDark) R.color.cyanea_switch_track_disabled_dark
        else R.color.cyanea_switch_track_disabled_light
    )
    val normal = ContextCompat.getColor(context,
        if (isDark) R.color.cyanea_switch_track_normal_dark
        else R.color.cyanea_switch_track_normal_light
    )
    return ColorStateList(
        arrayOf(
            intArrayOf(-attr.state_enabled),
            intArrayOf(
                attr.state_enabled,
                -attr.state_activated,
                -attr.state_checked
            ),
            intArrayOf(
                attr.state_enabled,
                attr.state_activated
            ),
            intArrayOf(
                attr.state_enabled,
                attr.state_checked
            )
        ),
        intArrayOf(disabled, normal, tint, tint)
    )
  }

}

/**
 * A [CyaneaViewProcessor] that styles [buttons][CompoundButton] in the overflow menu.
 */
@RequiresApi(Build.VERSION_CODES.M)
internal class CompoundButtonProcessor : CyaneaViewProcessor<CompoundButton>() {

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

@TargetApi(Build.VERSION_CODES.M)
internal class ImageButtonProcessor : CyaneaViewProcessor<ImageButton>() {

  override fun getType(): Class<ImageButton> = ImageButton::class.java

  override fun process(view: ImageButton, attrs: AttributeSet?, cyanea: Cyanea) {
    cyanea.tinter.tint(view.background)
  }

}

/**
 * Style menu items
 */
internal class ListMenuItemViewProcessor : CyaneaViewProcessor<ListMenuItemView>() {

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

@TargetApi(Build.VERSION_CODES.M)
internal class SearchAutoCompleteProcessor : CyaneaViewProcessor<SearchAutoComplete>() {

  override fun process(view: SearchAutoComplete, attrs: AttributeSet?, cyanea: Cyanea) {
    WidgetTint.setCursorColor(view, cyanea.accent)
  }

  override fun getType(): Class<SearchAutoComplete> = SearchAutoComplete::class.java

}

@TargetApi(Build.VERSION_CODES.M)
internal class SwitchProcessor : CyaneaViewProcessor<Switch>() {

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

internal class TextInputLayoutProcessor : CyaneaViewProcessor<TextInputLayout>() {

  override fun process(view: TextInputLayout, attrs: AttributeSet?, cyanea: Cyanea) {
    if (view.boxStrokeColor == Cyanea.getOriginalColor(R.color.color_accent_reference)) {
      view.boxStrokeColor = cyanea.accent
    }
  }

  override fun getType(): Class<TextInputLayout> = TextInputLayout::class.java

}

internal class TextViewProcessor : CyaneaViewProcessor<TextView>() {

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

@TargetApi(Build.VERSION_CODES.M)
internal class ViewGroupProcessor : CyaneaViewProcessor<ViewGroup>() {

  override fun process(view: ViewGroup, attrs: AttributeSet?, cyanea: Cyanea) {
    EdgeEffectTint.setEdgeGlowColor(view, cyanea.primary)
    cyanea.tinter.tint(view.background)
    if (view is AbsListView) {
      WidgetTint.setFastScrollThumbColor(view, cyanea.accent)
    }
  }

  override fun getType(): Class<ViewGroup> = ViewGroup::class.java

}
