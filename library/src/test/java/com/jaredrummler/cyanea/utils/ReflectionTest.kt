package com.jaredrummler.cyanea.utils

import com.jaredrummler.cyanea.utils.Reflection.Companion.getMethod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import kotlin.Int
import kotlin.NullPointerException
import kotlin.String
import kotlin.arrayOf

class ReflectionTest {

  @Test fun `should get method via reflection`() {
    val method = getMethod(StringBuilder(), "append", Int::class.java)

    val expectedName = "append"
    val expectedReturnType = StringBuilder::class.java

    assertEquals(expectedName, method?.name)
    assertEquals(expectedReturnType, method?.returnType)
  }

  @Test fun `should get field via reflection`() {
    val field = Reflection.getField(Class.forName("java.lang.Boolean"), "TRUE")

    val expectedType = "java.lang.Boolean"
    val expectedName = "TRUE"

    assertNotNull(field)
    assertEquals(expectedName, field?.name)
    assertEquals(expectedType, field?.type?.name)
  }

  @Test fun `should set field via reflection`() {
    val field = Reflection.getField(Class.forName("java.lang.Boolean"), "TRUE") ?: throw NullPointerException()
    Reflection.setFieldValue(field, null, java.lang.Boolean.FALSE)
    assertEquals(java.lang.Boolean.FALSE, java.lang.Boolean.TRUE)
  }

  @Test fun `should invoke method via reflection`() {
    val obj = "foo"
    val types = arrayOf<Class<*>>(CharSequence::class.java, CharSequence::class.java)
    val result = Reflection.invoke<String>(obj, "replace", types, "foo", "bar")
    assertEquals("bar", result)

    val string = "baz"
    val length = Reflection.invoke<Int>(string, "length")
    assertEquals(string.length, length)
  }

}
