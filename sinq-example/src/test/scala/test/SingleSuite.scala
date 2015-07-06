package test

import gen.STUDENT
import io.sinq.expression._
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class SingleSuite extends FunSuite with BeforeAndAfter {

  before {
    init()
  }
  after {
    latch.countDown()
  }
  test("Single.") {
    val query = sinq.select(STUDENT.id, STUDENT.name, STUDENT.age).from(STUDENT).where(Eq(STUDENT.id, 1))
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

