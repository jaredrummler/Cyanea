package com.jaredrummler.cyanea.inflator

import android.R.attr
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.CheckedTextView
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
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
   * @return True if this view should be processed.
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

internal class AlertDialogProcessor : CyaneaViewProcessor<View>() {

  override fun getType(): Class<View> = View::class.java

  override fun shouldProcessView(view: View): Boolean = view is AlertDialogLayout || CLASS_NAME == view.javaClass.name

  override fun process(view: View, attrs: AttributeSet?, cyanea: Cyanea) {
    view.setBackgroundColor(cyanea.backgroundColor) // Theme AlertDialog background
  }

  companion object {
    private const val CLASS_NAME = "com.android.internal.widget.AlertDialogLayout"
  }

}

internal class BottomAppBarProcessor : CyaneaViewProcessor<BottomAppBar>() {

  override fun getType(): Class<BottomAppBar> = BottomAppBar::class.java

  override fun process(view: BottomAppBar, attrs: AttributeSet?, cyanea: Cyanea) {
    view.backgroundTint?.let { view.backgroundTint = cyanea.tinter.tint(it) }
  }

}

internal class CheckedTextViewProcessor : CyaneaViewProcessor<CheckedTextView>() {

  override fun getType(): Class<CheckedTextView> = CheckedTextView::class.java

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

internal class DatePickerProcessor : CyaneaViewProcessor<DatePicker>() {

  override fun getType(): Class<DatePicker> = DatePicker::class.java

  override fun process(view: DatePicker, attrs: AttributeSet?, cyanea: Cyanea) {
    val date_picker_header = view.context.resources.getIdentifier("date_picker_header", "id", "android")
    if (date_picker_header != 0) {
      view.findViewById<ViewGroup>(date_picker_header)?.let { layout ->
        cyanea.tinter.tint(layout.background)
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
          cyanea.tinter.tint(layout.backgroundTintList)
        }
      }
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
internal class ListMenuItemViewProcessor : CyaneaViewProcessor<View>() {

  override fun getType(): Class<View> = View::class.java

  override fun shouldProcessView(view: View): Boolean = view is ListMenuItemView || view.javaClass.name == CLASS_NAME

  override fun process(view: View, attrs: AttributeSet?, cyanea: Cyanea) {
    cyanea.tinter.tint(view)
  }

  companion object {
    private const val CLASS_NAME = "com.android.internal.view.menu.ListMenuItemView"
  }

}

@TargetApi(Build.VERSION_CODES.M)
internal class SearchAutoCompleteProcessor : CyaneaViewProcessor<SearchAutoComplete>() {

  override fun getType(): Class<SearchAutoComplete> = SearchAutoComplete::class.java

  override fun process(view: SearchAutoComplete, attrs: AttributeSet?, cyanea: Cyanea) {
    WidgetTint.setCursorColor(view, cyanea.accent)
  }

}

@TargetApi(Build.VERSION_CODES.M)
internal class SwitchProcessor : CyaneaViewProcessor<Switch>() {

  override fun getType(): Class<Switch> = Switch::class.java

  @SuppressLint("PrivateResource")
  override fun process(view: Switch, attrs: AttributeSet?, cyanea: Cyanea) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      cyanea.tinter.tint(view.thumbDrawable)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      view.trackTintList = ContextCompat.getColorStateList(view.context, R.color.abc_tint_switch_track)
    }
  }

}

internal class TextInputLayoutProcessor : CyaneaViewProcessor<TextInputLayout>() {

  override fun getType(): Class<TextInputLayout> = TextInputLayout::class.java

  override fun process(view: TextInputLayout, attrs: AttributeSet?, cyanea: Cyanea) {
    if (view.boxStrokeColor == Cyanea.getOriginalColor(R.color.color_accent_reference)) {
      view.boxStrokeColor = cyanea.accent
    }
  }

}

internal class TextViewProcessor : CyaneaViewProcessor<TextView>() {

  override fun getType(): Class<TextView> = TextView::class.java

  override fun process(view: TextView, attrs: AttributeSet?, cyanea: Cyanea) {
    view.textColors?.let { colors ->
      view.setTextColor(cyanea.tinter.tint(colors))
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      cyanea.tinter.tint(view.backgroundTintList)
    }
    cyanea.tinter.tint(view.background)
  }

}

internal class TimePickerProcessor : CyaneaViewProcessor<TimePicker>() {

  override fun getType(): Class<TimePicker> = TimePicker::class.java

  override fun process(view: TimePicker, attrs: AttributeSet?, cyanea: Cyanea) {
    cyanea.tinter.tint(view)
  }

}

@TargetApi(Build.VERSION_CODES.M)
internal class ViewGroupProcessor : CyaneaViewProcessor<ViewGroup>() {

  override fun getType(): Class<ViewGroup> = ViewGroup::class.java

  override fun process(view: ViewGroup, attrs: AttributeSet?, cyanea: Cyanea) {
    EdgeEffectTint.setEdgeGlowColor(view, cyanea.primary)
    cyanea.tinter.tint(view.background)
    if (view is AbsListView) {
      WidgetTint.setFastScrollThumbColor(view, cyanea.accent)
    }
  }

}
