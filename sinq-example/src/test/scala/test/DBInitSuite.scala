package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.ImplicitsSinq.sinq2Count
import init.STUDENT
import io.sinq.SinqStream
import io.sinq.provider.JPA
import models.{Husband, Student, Teacher}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class DBInitSuite extends FunSuite with BeforeAndAfter {

  before {
    JPA.initPersistenceName("postgres")
  }
  after {
    JPA.release()
  }

  test("DB Init.") {
    val sinq = SinqStream("postgres")
    val latch = new CountDownLatch(6)
    Future {
      (0 to 2).foreach {
        i =>
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
          sinq.select().from(STUDENT).where().collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
          latch.countDown()
      }
    }
    Future {
      println(1)
      (0 to 2).foreach {
        i =>
          println(2)
          val count = sinq.count(classOf[Student])
          println(3)
          println(s"count:${count}")
          if (count <= 10) {
            val teacher = Teacher("习大大", 999, "BeiJing")
            sinq.insert(teacher)
            val husband = Husband("中国梦", 999)
            husband.setTeacher(teacher)
            sinq.insert(husband)
            (0 to 10).foreach(i => sinq.insert(new Student(s"YaFengli:${i}", i, s"NanJing:${i}", teacher)))
          }
          sinq.select().from(STUDENT).where().collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
          latch.countDown()
      }
    }
    latch.await(20, TimeUnit.SECONDS)
  }
}


