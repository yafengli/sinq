package test

import org.h2.tools.Server

import scala.concurrent.forkjoin.ForkJoinPool

object H2DB {
  val server = Server.createTcpServer()

  lazy val pool = new ForkJoinPool(8)

  def open {
    if (!server.isRunning(false)) server.start()
    println("##########start###############")
  }

  def close {
    server.stop()
    println("##########end###############")
  }
}