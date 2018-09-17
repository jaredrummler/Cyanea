package com.jaredrummler.cyanea

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorInt
import com.jaredrummler.cyanea.utils.Reflection
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CyaneaResourcesTest {

  @Mock lateinit var app: Application
  @Mock lateinit var res: Resources

  lateinit var cyanea: Cyanea

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
    whenever(app.getSharedPreferences(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
        .thenReturn(Mockito.mock(SharedPreferences::class.java))
    whenever(app.getSharedPreferences(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()).edit())
        .thenReturn(Mockito.mock(SharedPreferences.Editor::class.java))
    Cyanea.init(app, res)
    cyanea = Cyanea.instance
  }

  @Test fun `should return color drawables for background colors`() {
    val resources = CyaneaResources(res)

    val drawableIds = arrayOf(
        R.drawable.color_background_dark,
        R.drawable.color_background_dark_lighter,
        R.drawable.color_background_dark_darker,
        R.drawable.color_background_light,
        R.drawable.color_background_light_lighter,
        R.drawable.color_background_light_darker
    )

    drawableIds.forEach { resid ->
      val drawable = resources.getDrawable(resid)
      assertThat(drawable, instanceOf(ColorDrawable::class.java))
    }
  }

  @Test fun `cyanea resources should return cyanea primary color`() {
    val resources = CyaneaResources(res)
    setCyaneaColor(0xFF0099CC.toInt(), "primary")
    val color = resources.getColor(R.color.color_primary)
    assertEquals(cyanea.primary, color)
  }

  @Test fun `cyanea resources should return cyanea accent color`() {
    val resources = CyaneaResources(res)
    setCyaneaColor(0xFF0099CC.toInt(), "accent")
    val color = resources.getColor(R.color.color_accent)
    assertEquals(cyanea.accent, color)
  }

  private fun setCyaneaColor(@ColorInt color: Int, name: String) {
    Reflection.getField(cyanea, name)?.let { field ->
      Reflection.setFieldValue(field, cyanea, color)
    }
  }

}