package test

import init.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SingleSuite extends FunSuite with BeforeAndAfter {

  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }
  test("Single.") {
    val sinq = SinqStream("h2")

    val query = sinq.select(STUDENT.* : _*).from(STUDENT).where(Eq(STUDENT.id, 1))
    query.single() match {
      case Some(Array(id, name, age)) => println(s"#id:${id} name:${name} age:${age}")
      case None => println("None")
    }
    sinq.find(1L, classOf[Student]) match {
      case Some(s) => println(s">id:${s.id} name:${s.name} age:${s.age}")
      case None =>
    }
  }
}

