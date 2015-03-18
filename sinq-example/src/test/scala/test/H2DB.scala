package test

import io.sinq.provider.JPA
import org.h2.tools.Server

object H2DB {
  val server = Server.createTcpServer()

  def open {
    server.start()
    JPA.initPersistenceName("h2")
    println("##########H2 DB Server is started.###############")
  }

  def close {
    server.stop()
    server.shutdown()
    JPA.release()
    println("##########H2 DB Server is closed.###############")
  }
}

