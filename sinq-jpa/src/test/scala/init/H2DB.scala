package init

import io.sinq.provider.JPA
import org.h2.tools.Server

object H2DB {
  val server = Server.createTcpServer()

  def open {
    if (!server.isRunning(false)) server.start()
    JPA.initPersistenceName("h2")
    println("##########H2 DB Server is started.###############")
  }

  def close {
    JPA.release()
    server.stop()
    println("##########H2 DB Server is closed.###############")
  }
}


