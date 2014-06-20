package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class DemoSuite extends FunSuite with BeforeAndAfter {
  before {
    println("##########before##############")
  }
  after {
    println("##########after##############")
  }

  test("Hello World!") {
    (0 to 5).foreach(i => println(s"[${i}]Hello World!"))
  }
}
