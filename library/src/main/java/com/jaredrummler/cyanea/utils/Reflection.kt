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

package com.jaredrummler.cyanea.utils

import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Contains helper methods to get/set fields/methods via reflection
 */
class Reflection private constructor() {

  init {
    throw AssertionError("no instances")
  }

  companion object {

    private val CACHE = mutableMapOf<String, AccessibleObject>()

    /**
     * Dynamically invoke a method.
     *
     * @param obj
     *     The object or class to get the method from.
     * @param name
     *     The name of the method to invoke.
     * @param types
     *     the parameter types of the requested method.
     * @param args
     *     the arguments to the method
     * @param <T>
     *     the method's return type
     * @return the result of dynamically invoking this method.
     */
    @JvmStatic
    fun <T> invoke(
      obj: Any?,
      name: String,
      types: Array<Class<*>> = emptyArray(),
      vararg args: Any
    ): T? {
      try {
        val method = getMethod(obj, name, *types)
        method?.let {
          @Suppress("UNCHECKED_CAST")
          return method.invoke(obj, *args) as? T
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return null
    }

    /**
     * Get the value from a field in an object/class.
     *
     * @param obj
     *     the object or class
     * @param name
     *     the name of the field
     * @param <T>
     *     The field's type
     * @return the value of the field in the specified object.
     */
    @JvmStatic
    fun <T> getFieldValue(obj: Any?, name: String): T? {
      val field = getField(obj, name)
      field?.let {
        if (!field.isAccessible) field.isAccessible = true
        try {
          @Suppress("UNCHECKED_CAST")
          return field.get(obj) as? T
        } catch (e: IllegalAccessException) {
          e.printStackTrace()
        }
      }
      return null
    }

    /**
     * Set the field value to the specified value.
     *
     * @param field
     *     the field
     * @param obj
     *     the object whose field should be modified
     * @param value
     *     the new value for the field of `obj` being modified
     * @return `true` if the field was set successfully.
     */
    @JvmStatic
    fun setFieldValue(field: Field, obj: Any?, value: Any?): Boolean {
      if (!field.isAccessible) {
        field.isAccessible = true
      }
      try {
        if (Modifier.isFinal(field.modifiers)) {
          val modifiersField = Field::class.java.getDeclaredField("modifiers")
          modifiersField.isAccessible = true
          modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
        }
        field.set(obj, value)
        return true
      } catch (e: IllegalAccessException) {
        e.printStackTrace()
      }
      return false
    }

    /**
     * Get a method from a class
     *
     * @param obj
     *     the object or class.
     * @param name
     *     the requested method's name
     * @param types
     *     the parameter types of the requested method.
     * @return a [Method] object which represents the method matching the specified name and parameter types
     */
    @JvmStatic
    fun getMethod(obj: Any?, name: String, vararg types: Class<*>): Method? {
      if (obj == null) return null

      val key = cacheKey(obj, name, *types)
      var method: Method? = CACHE[key] as? Method
      if (method != null) return method

      var klass: Class<*>? = obj as? Class<*> ?: obj.javaClass

      while (klass != null) {
        try {
          method = klass.getDeclaredMethod(name, *types).also { it.isAccessible = true }
          break
        } catch (ignored: NoSuchMethodException) {
        }
        klass = klass.superclass
      }

      method?.let { CACHE.put(key, method) }

      return method
    }

    /**
     * Get a field from a class.
     *
     * @param obj
     *     the object or class
     * @param name
     *     the name of the field
     * @return a [Field] object for the field with the given name which is declared in the class.
     */
    @JvmStatic
    fun getField(obj: Any?, name: String): Field? {
      if (obj == null) return null

      val key = cacheKey(obj, name)
      var field: Field? = CACHE[key] as? Field
      if (field != null) return field

      var klass: Class<*>? = obj as? Class<*> ?: obj.javaClass

      do {
        try {
          field = klass?.getDeclaredField(name)
          if (field != null) {
            field.isAccessible = true
          }
        } catch (ignored: NoSuchFieldException) {
        }
        if (field == null) {
          klass = klass?.superclass
        }
      } while (field == null && klass != null)

      field?.let { CACHE.put(key, it) }

      return field
    }

    private fun cacheKey(obj: Any, name: String, vararg types: Class<*>): String {
      val klass = obj as? Class<*> ?: obj::class.java
      val key = StringBuilder(klass.name)
      key.append('#')
      key.append(name)
      if (types.isNotEmpty()) {
        var separator = ""
        key.append('(')
        types.forEach {
          key.append(separator)
          key.append(it.name)
          separator = ", "
        }
        key.append(')')
      }
      return key.toString()
    }
  }
}
