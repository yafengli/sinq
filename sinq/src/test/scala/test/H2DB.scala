package test

import org.h2.tools.Server
import org.koala.sporm.SpormFacade
import org.koala.sporm.jpa.JPA

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
    JPA.release()
    server.stop()
    println("##########end###############")
  }
}
