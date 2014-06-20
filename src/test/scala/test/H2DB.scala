package test

import org.h2.tools.Server
import org.koala.sporm.jpa.JPA

object H2DB {
  val server = Server.createTcpServer()

  def open {
    if (!server.isRunning(false)) server.start()
    JPA.initPersistenceName("default")
    println("##########start###############")
  }

  def close {
    server.stop()
    println("##########end###############")
  }
}
