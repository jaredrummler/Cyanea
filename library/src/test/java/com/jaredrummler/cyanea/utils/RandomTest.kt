package com.jaredrummler.cyanea.utils

import org.junit.Test

class RandomTest {

  var foo: Int? = 1

  @Test
  fun test() {


    println(b())
  }

  fun b(): Boolean {
    return foo as? Int == 1
  }

}