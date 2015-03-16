package test

import io.sinq.SinqStream
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

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
    if (count <= 0) {
      sinq.insert(Student("YaFengli", 999, "NanJing"))
    }

    println(s"count:${count}")
    sinq.select().from(_table).collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
  }
}


