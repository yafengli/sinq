package test

import java.math.BigInteger
import java.util.concurrent.{CountDownLatch, TimeUnit}

import init._USER
import io.sinq.SinqStream
import io.sinq.func.Count
import io.sinq.util.JPA
import models.User
import org.h2.tools.Server

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object H2DB {
  // Test Method Number
  val count = 1
  val latch = new CountDownLatch(count)
  val server = Server.createTcpServer()

  def init(): Unit = {
    if (latch.getCount == count) {
      if (!server.isRunning(false)) {
        println(s"##########DB Server start.###############")
      }
      JPA.initPersistenceName("h2") //JPA.initPersistenceName("h2", "postgres")
      //data init
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
    val count = sinq.select(Count(_USER.id)).from(_USER).single().getOrElse(BigInteger.valueOf(0))
    if (count.longValue() <= 0) {
      (0 to 5).foreach {
        i =>
          val user = User(s"user-${i}", i)
          sinq.insert(user)
          val address = models.Address(s"NanJing:${i}", i)
          address.setUser(user)
          sinq.insert(address)
      }
    }
  }
}