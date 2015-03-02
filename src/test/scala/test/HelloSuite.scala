package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class HelloSuite extends FunSuite with BeforeAndAfter {
  before {
  }

  after {
  }

  test("hello") {
    IA.sayHi("AA")
    IA.sayHello("IA")
  }
}


abstract class SayHi[T: Manifest] {
  def sayHi(name: String): Unit = println(s"Hi ${name}")

  def sayHello(name: String): Unit
}


case class A(val name: String, val id: Long)

object IA extends SayHi {
  override def sayHello(name: String): Unit = println("A")

  def apply(name: String): A = {
    A(name, -1)
  }
}