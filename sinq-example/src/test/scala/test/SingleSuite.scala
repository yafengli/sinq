package test

import gen._STUDENT
import io.sinq.expr._
import models.Student
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import test.PGDBInit._

@RunWith(classOf[JUnitRunner])
class SingleSuite extends FunSuite with BeforeAndAfter {

  before {
    init()
  }
  after {
    latch.countDown()
  }
  test("Single.") {
    val query = sinq.select(_STUDENT.id, _STUDENT.name, _STUDENT.age).from(_STUDENT).where(Eq(_STUDENT.id, 1))
    query.single() match {
      case Some((id, name, age)) => println(s"#id:${id} name:${name} age:${age}")
      case None => println("None")
    }
    sinq.find(1L, classOf[Student]) match {
      case Some(s) => println(s">id:${s.id} name:${s.name} age:${s.age}")
      case None =>
    }
  }
}

