package com.jaredrummler.cyanea.utils

import com.jaredrummler.cyanea.utils.Reflection.Companion.getMethod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.lang.CharSequence
import java.lang.StringBuilder
import java.lang.reflect.Method
import kotlin.Int
import kotlin.NullPointerException
import kotlin.String
import kotlin.arrayOf


class ReflectionTest {

  @Test
  fun should_get_method_via_reflection() {
    val method: Method? = getMethod(StringBuilder(), "append", Int::class.java)

    val expectedName = "append"
    val expectedReturnType = StringBuilder::class.java

    assertEquals(expectedName, method?.name)
    assertEquals(expectedReturnType, method?.returnType)
  }

  @Test
  fun should_get_field_via_reflection() {
    val field = Reflection.getField(Class.forName("java.lang.Boolean"), "TRUE")

    val expectedType = "java.lang.Boolean"
    val expectedName = "TRUE"

    assertNotNull(field)
    assertEquals(expectedName, field?.name)
    assertEquals(expectedType, field?.type?.name)
  }

  @Test
  fun should_set_field_via_reflection() {
    val field = Reflection.getField(Class.forName("java.lang.Boolean"), "TRUE") ?: throw NullPointerException()
    Reflection.setFieldValue(field, null, java.lang.Boolean.FALSE)
    assertEquals(java.lang.Boolean.FALSE, java.lang.Boolean.TRUE)
  }

  @Test
  fun should_invoke_method_via_reflection() {
    val obj = "foo"
    val types = arrayOf<Class<*>>(CharSequence::class.java, CharSequence::class.java)
    val result = Reflection.invoke<String>(obj, "replace", types, "foo", "bar")
    assertEquals("bar", result)
  }

}