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
    println("Hello World!")
    println("Hello World!")
    println("Hello World!")
  }
}
