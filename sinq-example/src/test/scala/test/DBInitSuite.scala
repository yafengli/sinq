package test

import init.STUDENT
import io.sinq.SinqStream
import models.{Husband, Student, Teacher}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class DBInitSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream("h2")

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
      val teacher = Teacher("习大大", 999, "BeiJing")
      sinq.insert(teacher)
      val husband = Husband("中国梦", 999)
      husband.setTeacher(teacher)
      sinq.insert(husband)
      (0 to 10).foreach(i => sinq.insert(new Student(s"YaFengli:${i}", i, s"NanJing:${i}", teacher)))
    }
    println(s"count:${count}")
    sinq.select().from(STUDENT).where().collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
  }
}


