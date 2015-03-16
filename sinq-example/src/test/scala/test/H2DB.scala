package test

import io.sinq.jpa.JPA
import org.h2.tools.Server
import org.koala.sporm.SpormFacade

import scala.concurrent.forkjoin.ForkJoinPool

object H2DB {
  val server = Server.createTcpServer()

  lazy val facade = SpormFacade("default")
  lazy val pool = new ForkJoinPool(8)

  def open {
    if (!server.isRunning(false)) server.start()
    JPA.initPersistenceName("default")
    println("##########start###############")
  }

  def close {
    server.stop()
    JPA.release()
    println("##########end###############")
  }
}
