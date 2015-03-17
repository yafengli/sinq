package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class StringSuite extends FunSuite with BeforeAndAfter {


  test("Seq split") {
    println(split(Seq(1).toList))
    println(split(Seq(1, 2).toList))
    println(split(Seq(1, 2, 3).toList))
    println(split(Seq(1, 2, 3, 4).toList))
    println(split(Seq(1, 2, 3, 4, 5).toList))
  }

  private def split(list: List[Any]): String = {
    list match {
      case Nil => ""
      case last :: Nil => "?"
      case head :: tails =>
        val buffer = new StringBuffer("?")
        (0 until tails.size).foreach(t => buffer.append(",?"))
        buffer.toString
    }
  }

}
