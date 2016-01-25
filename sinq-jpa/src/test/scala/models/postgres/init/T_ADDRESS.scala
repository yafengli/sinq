package models.postgres.init

import java.util.Date

import io.sinq.provider.{Column, Table}
import models.postgres.Address

object T_ADDRESS extends Table[Address]("t_address") {
  def id = Column(this, classOf[Long], "id")

  def name = Column(this, classOf[String], "name")

  def createDate = Column(this, classOf[Date], "createdate")

  def num = Column(this, classOf[Int], "num")

  def u_id = Column(this, classOf[Long], "u_id")
}
