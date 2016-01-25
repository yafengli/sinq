package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import models.postgres.init.ImplicitsSinq.sinq2Count
import io.sinq.SinqStream
import io.sinq.util.JPA
import models.{Husband, Student, Teacher}
import org.h2.tools.Server

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object H2DB {
  // Test Method Number
  val count = 6
  val latch = new CountDownLatch(count)
  val server = Server.createTcpServer()
  lazy val sinq = SinqStream("h2")

  def init(): Unit = {
    if (latch.getCount == count) {
      if (!server.isRunning(false)) {
        println(s"##########DB Server start.###############")
      }
      JPA.initPersistenceName("h2")
      //data models.postgres.init
      dataStore()
      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.release()
        println(s"##########DB Server closed.###############")
      }
    }
  }

  private def dataStore(): Unit = {
    val sinq = SinqStream("h2")
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