package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import io.sinq.SinqStream
import io.sinq.util.JPA
import models.postgres.init.ImplicitsSinq.sinq2Count
import models.{Husband, Student, Teacher}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object H2DB {
  val pn = "postgres"
  implicit lazy val sinq = SinqStream(pn)

  val count = 5
  val latch = new CountDownLatch(count)

  def init(): Unit = {
    if (latch.getCount == count) {
      println(s"##########DB Server start.###############")
      JPA.initPersistenceName(pn)
      //data models.postgres.init
      dataStore
      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.release()
        println(s"##########DB Server closed.###############")
      }
    }
  }

  private def dataStore(implicit sinq: SinqStream): Unit = {
    val count = sinq.count(classOf[Student])
    if (count <= 10) {
      val teacher = Teacher("习大大", 999, "BeiJing")
      sinq.insert(teacher)
      val husband = Husband("中国梦", 999)
      husband.setTeacher(teacher)
      sinq.insert(husband)
      (0 to 10).foreach(i => sinq.insert(new Student(s"YaFengli:${i}", i, s"NanJing:${i}", teacher)))
    }
  }
}