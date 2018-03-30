package com.jaredrummler.cyanea

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
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

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)
    Mockito.`when`(app.getSharedPreferences(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
        .thenReturn(Mockito.mock(SharedPreferences::class.java))
    Mockito.`when`(app.getSharedPreferences(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()).edit())
        .thenReturn(Mockito.mock(SharedPreferences.Editor::class.java))
    Cyanea.init(app, res)
    cyanea = Cyanea.instance
  }

  @Test
  fun should_return_color_drawables_for_background_colors() {
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

  @Test
  fun cyanea_resources_should_return_cyanea_primary_color() {
    val resources = CyaneaResources(res)

    run {
      cyanea.edit().primary(0xFF0099CC.toInt()).apply()
      val color = resources.getColor(R.color.color_primary)
      assertEquals(cyanea.primary, color)
    }

    run {
      cyanea.edit().primary(0xFF99CC00.toInt()).apply()
      val color = resources.getColor(R.color.color_primary)
      assertEquals(cyanea.primary, color)
    }
  }

  @Test
  fun cyanea_resources_should_return_cyanea_accent_color() {
    val resources = CyaneaResources(res)

    run {
      cyanea.edit().accent(0xFF0099CC.toInt()).apply()
      val color = resources.getColor(R.color.color_accent)
      assertEquals(cyanea.accent, color)
    }

    run {
      cyanea.edit().accent(0xFF99CC00.toInt()).apply()
      val color = resources.getColor(R.color.color_accent)
      assertEquals(cyanea.accent, color)
    }
  }

}