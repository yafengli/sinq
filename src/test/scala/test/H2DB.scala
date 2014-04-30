package test

import org.h2.tools.Server
import org.koala.sporm.jpa.JPA

object H2DB {
  val server = Server.createTcpServer()

  def init = {
    println("##########start###############")
    if (!server.isRunning(false)) server.start()
    JPA.initPersistenceName("default")
    "Init db"
  }

  def close = {
    println("##########end###############")
    server.shutdown()
    "close db"
  }
}
