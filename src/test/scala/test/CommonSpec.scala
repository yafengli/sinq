package test

import org.specs2.mutable

/**
 * User: Administrator
 * Date: 13-2-22
 * Time: 上午11:28
 */
class CommonSpec extends mutable.Specification {
  "Test common" should {
    val c = new Common
    c.name = "hello"
    c.age = 23
    println(f"${c.name} ${c.age} ${c.num}")
  }
}

class Common {
  var name: String = _
  var age: Int = _
  var num: Long = _
}
