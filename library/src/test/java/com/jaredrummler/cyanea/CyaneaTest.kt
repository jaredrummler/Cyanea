package com.jaredrummler.cyanea

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import com.jaredrummler.cyanea.Cyanea.BaseTheme
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CyaneaTest {

  @Mock private lateinit var application: Application
  @Mock private lateinit var resources: Resources

  @Before fun setUp() {
    whenever(application.getSharedPreferences(anyString(), anyInt()))
        .thenReturn(mock(SharedPreferences::class.java))

    Cyanea.init(application, resources)
  }

  @Test fun `should return singleton instance`() {
    assertNotNull(Cyanea.instance)
  }

  @Test fun `should return dark background when base theme is dark`() {
    val cyanea = Cyanea.instance
    val expectedColor = 0xFF000000.toInt()

    cyanea.apply {
      baseTheme = BaseTheme.DARK
      backgroundDark = 0xFF000000.toInt()
    }

    assertEquals(expectedColor, cyanea.backgroundColor)
  }

  @Test fun `should return light background when base theme is light`() {
    val cyanea = Cyanea.instance
    val expectedColor = 0xFFFFFFFF.toInt()

    cyanea.apply {
      baseTheme = BaseTheme.LIGHT
      backgroundLight = 0xFFFFFFFF.toInt()
    }

    assertEquals(expectedColor, cyanea.backgroundColor)
  }

}
