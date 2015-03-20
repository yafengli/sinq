package test

import init.ImplicitsSinq.sinq2Count
import init.STUDENT
import io.sinq.SinqStream
import models.{Husband, Student, Teacher}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class DBInitSuite extends FunSuite with BeforeAndAfter {

  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }

  test("DB Init.") {
    val sinq = SinqStream("h2")

    val count = sinq.count(classOf[Student])
    println(s"count:${count}")
    if (count <= 10) {
      val teacher = Teacher("习大大", 999, "BeiJing")
      sinq.insert(teacher)
      val husband = Husband("中国梦", 999)
      husband.setTeacher(teacher)
      sinq.insert(husband)
      (0 to 10).foreach(i => sinq.insert(new Student(s"YaFengli:${i}", i, s"NanJing:${i}", teacher)))
    }
    sinq.select().from(STUDENT).where().collect(classOf[Student]).foreach(s => println(s"id:${s.id} teacher:${s.teacher.name}"))
  }
}


