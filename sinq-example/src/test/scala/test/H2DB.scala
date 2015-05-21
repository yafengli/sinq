package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import io.sinq.util.JPA
import org.h2.tools.Server

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object H2DB {
  // Test Method Number
  val count = 7
  val latch = new CountDownLatch(count)
  val server = Server.createTcpServer()

  def init(): Unit = {
    if (latch.getCount == count) {
      if (!server.isRunning(false)) {
        println(s"##########DB Server start.###############")
      }
      JPA.initPersistenceName("h2")
      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.release()
        println(s"##########DB Server closed.###############")
      }
    }
  }
}