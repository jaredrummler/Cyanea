package com.jaredrummler.cyanea

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import com.jaredrummler.cyanea.utils.Reflection
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class CyaneaResourcesTest {

  @Mock private lateinit var app: Application
  @Mock private lateinit var res: Resources
  private lateinit var cyanea: Cyanea

  @Before fun setUp() {
    whenever(app.getSharedPreferences(anyString(), anyInt()))
        .thenReturn(mock(SharedPreferences::class.java))
    Cyanea.init(app, res)
    cyanea = Cyanea.instance
  }

  @Test fun `should return color drawables for background colors`() {
    val resources = CyaneaResources(res)

    val drawableIds = arrayOf(
        R.drawable.cyanea_bg_dark,
        R.drawable.cyanea_bg_dark_lighter,
        R.drawable.cyanea_bg_dark_darker,
        R.drawable.cyanea_bg_light,
        R.drawable.cyanea_bg_light_lighter,
        R.drawable.cyanea_bg_light_darker
    )

    drawableIds.forEach { resid ->
      @Suppress("DEPRECATION")
      val drawable = resources.getDrawable(resid)
      assertThat(drawable, instanceOf(ColorDrawable::class.java))
    }
  }

  @Test fun `cyanea resources should return cyanea primary color`() {
    val resources = CyaneaResources(res)

    setCyaneaColor(0xFF0099CC.toInt(), "primary")
    @Suppress("DEPRECATION")
    val color = resources.getColor(R.color.cyanea_primary)

    assertEquals(cyanea.primary, color)
  }

  @Test fun `cyanea resources should return cyanea accent color`() {
    val resources = CyaneaResources(res)

    setCyaneaColor(0xFF0099CC.toInt(), "accent")
    @Suppress("DEPRECATION")
    val color = resources.getColor(R.color.cyanea_accent)

    assertEquals(cyanea.accent, color)
  }

  private fun setCyaneaColor(@ColorInt color: Int, name: String) {
    Reflection.getField(cyanea, name)?.let { field ->
      Reflection.setFieldValue(field, cyanea, color)
    }
  }

}
