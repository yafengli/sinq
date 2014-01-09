package test

import org.specs2._

/**
 * User: Administrator
 * Date: 13-2-22
 * Time: 上午11:28
 */
class CommonSpec extends Specification {

  def is = s2"""

 This is a specification to check the 'Hello world' string

 The 'Hello world' string should
   contain 11 characters                             $call
                                                     """

  def call = {
    val c = new Common
    c.name = "hello"
    c.age = 23
    println(f"${c.name} ${c.age} ${c.num}")
    "Call" must have size (4)
  }
}

class Common {
  var name: String = _
  var age: Int = _
  var num: Long = _
}
