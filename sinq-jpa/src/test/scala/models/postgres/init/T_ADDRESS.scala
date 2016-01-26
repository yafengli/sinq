package models.postgres.init

import java.net.InetAddress
import java.util.Date

import io.sinq.provider.Table
import models.postgres.Address

object T_ADDRESS extends Table[Address]("t_address") {
  def id = column("id", classOf[Long])

  def name = column("name", classOf[String])

  def createDate = column("createdate", classOf[Date])

  def ipAddr = column("ipaddr", classOf[InetAddress])

  def num = column("num", classOf[Int])

  def u_id = column("u_id", classOf[Long])
}
