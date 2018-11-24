/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.cyanea

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import com.jaredrummler.cyanea.utils.Reflection
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class CyaneaResourcesTest {

  @Mock private lateinit var app: Application
  @Mock private lateinit var res: Resources
  private lateinit var cyanea: Cyanea

  @Before fun setUp() {
    whenever(app.getSharedPreferences(anyString(), anyInt()))
        .thenReturn(mock())
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
