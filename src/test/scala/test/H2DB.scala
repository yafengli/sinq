package test

import org.h2.tools.Server

/**
 * Created by Administrator on 14-1-14.
 */
object H2DB {
  val server = Server.createTcpServer()

  def init = {
    println("##########start###############")
    server.start()
    "Init db"
  }

  def close = {
    println("##########end###############")
    server.stop()
    "close db"
  }
}
