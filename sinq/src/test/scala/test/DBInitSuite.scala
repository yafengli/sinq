package test

import init.{STUDENT, H2DB}
import io.sinq.SinqStream
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import H2DB._

@RunWith(classOf[JUnitRunner])
class DBInitSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()

  before {
    open
  }

  after {
    close
  }

  test("DB Init.") {
    val count = sinq.count(classOf[Student])
    if (count <= 10) {
      (0 to 10).foreach(i => sinq.insert(Student(s"YaFengli:${i}", i, s"NanJing:${i}")))
    }

    println(s"count:${count}")
    sinq.select().from(STUDENT).collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
  }
}


