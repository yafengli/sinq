package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import io.sinq.SinqStream
import io.sinq.util.JPA
import models.postgres.init.ImplicitsSinq.sinq2Count
import models.{Husband, Student, Teacher}
import org.h2.tools.Server

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object H2DB {
  implicit lazy val sinq_pg = SinqStream("postgres")
  implicit lazy val sinq_h2 = SinqStream("h2")

  val count = 6
  val latch = new CountDownLatch(count)


  def init(): Unit = {
    if (latch.getCount == count) {
      println(s"##########DB Server start.###############")
      val server = Server.createTcpServer("-tcpPort", "9999")
      server.start()

      println(s"${server.getStatus} ${server.getURL}")
      dataStore(sinq_h2)
      dataStore(sinq_pg)

      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.releaseAll()
        server.stop()
        println(s"##########DB Server closed.###############")
      }
    }
  }

  private def dataStore(sinq: SinqStream): Unit = {
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
