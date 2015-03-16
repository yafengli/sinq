package test

import io.sinq.jpa.JPA
import io.sinq.rs.{Column, Table}
import org.h2.tools.Server

object H2DB {
  val server = Server.createTcpServer()

  val _table = Table("t_student", "t")
  val _id = Column(_table, "id")
  val _name = Column(_table, "name")
  val _age = Column(_table, "age")
  val _all = Column(_table, "id", "name")

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
