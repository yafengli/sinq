package test

import init.H2DB._
import init.{H2DB, STUDENT}
import io.sinq.SinqStream
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

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
    import init.ImplicitsSinq.sinq2Count
    
    val count = sinq.count(classOf[Student])
    if (count <= 10) {
      (0 to 10).foreach(i => sinq.insert(Student(s"YaFengli:${i}", i, s"NanJing:${i}")))
    }

    println(s"count:${count}")
    sinq.select().from(STUDENT).collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
  }
}


