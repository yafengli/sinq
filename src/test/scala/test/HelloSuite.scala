package test


import demo.v.IA
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class HelloSuite extends FunSuite with BeforeAndAfter {
  before {
  }

  after {
  }
  test("hello") {
    parProc("AA")
    parProc("BB")
    val t = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    println(t.getClass)
  }

  def parProc(name: String): Unit = {
    import demo.v.IA._
    Future {
      val a = IA(name)
      (0 to 10).foreach {
        i =>
          a.sayHi(s":${a.name}[${i}]")
          Thread.sleep(1000)
      }
    }
  }
}
