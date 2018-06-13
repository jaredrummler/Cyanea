package com.jaredrummler.cyanea

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import com.jaredrummler.cyanea.Cyanea.BaseTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CyaneaTest {

  @Mock lateinit var application: Application
  @Mock lateinit var resources: Resources

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)
  }

  @Test(expected = Exception::class)
  fun should_throw_exception_if_singleton_is_accessed_before_init() {
    Cyanea.instance
  }

  @Test
  fun should_return_singleton_instance() {
    Cyanea.init(application, resources)
    Mockito.`when`(application.getSharedPreferences(anyString(), anyInt()))
        .thenReturn(Mockito.mock(SharedPreferences::class.java))
    assertNotNull(Cyanea.instance)
  }

  @Test
  fun should_return_dark_background_when_base_theme_is_dark() {
    Cyanea.init(application, resources)
    Mockito.`when`(application.getSharedPreferences(anyString(), anyInt()))
        .thenReturn(Mockito.mock(SharedPreferences::class.java))
    val cyanea = Cyanea.instance
    val expectedColor = 0xFF000000.toInt()
    cyanea.baseTheme = BaseTheme.DARK
    cyanea.backgroundDark = 0xFF000000.toInt()
    assertEquals(expectedColor, cyanea.backgroundColor)
  }

  @Test
  fun should_return_light_background_when_base_theme_is_light() {
    Cyanea.init(application, resources)
    Mockito.`when`(application.getSharedPreferences(anyString(), anyInt()))
        .thenReturn(Mockito.mock(SharedPreferences::class.java))
    val cyanea = Cyanea.instance
    val expectedColor = 0xFFFFFFFF.toInt()
    cyanea.baseTheme = BaseTheme.LIGHT
    cyanea.backgroundLight = 0xFFFFFFFF.toInt()
    assertEquals(expectedColor, cyanea.backgroundColor)
  }

}