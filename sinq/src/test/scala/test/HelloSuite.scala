package test


import demo.v.LineNumber
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

    val t = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    println(t.getClass)
    import demo.v.ImplicitsObj._
    val a = new LineNumber(111)
    val b = new LineNumber(222)

    println((a + b).num)
    println(1 + a)
    println(1 + b)

    println(a.num)
    println(b.num)
  }

}


