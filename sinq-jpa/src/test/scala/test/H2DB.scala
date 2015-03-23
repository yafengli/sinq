package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import io.sinq.util.JPA
import org.h2.tools.Server

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object H2DB {
  // Test Method Number
  val count = 2
  val latch = new CountDownLatch(count)
  val server = Server.createTcpServer()

  def init(): Unit = {
    if (latch.getCount == count) {
      if (!server.isRunning(false)) {
//        server.start()
        println(s"##########DB Server start.###############")
      }
      JPA.initPersistenceName("h2", "postgres")
      Future {
        latch.await(30, TimeUnit.SECONDS)
        JPA.release()
//        server.stop()
        println(s"##########DB Server closed.###############")
      }
    }
  }
}