package com.jaredrummler.cyanea.inflator.processors

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.widget.SearchView.SearchAutoComplete
import android.util.AttributeSet
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.tinting.WidgetTint

@TargetApi(Build.VERSION_CODES.M)
class SearchAutoCompleteProcessor : CyaneaViewProcessor<SearchAutoComplete>() {

  override fun process(view: SearchAutoComplete, attrs: AttributeSet?, cyanea: Cyanea) {
    WidgetTint.setCursorColor(view, cyanea.accent)
  }

  override fun getType(): Class<SearchAutoComplete> = SearchAutoComplete::class.java

}