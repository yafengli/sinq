package models.h2.init

import java.util.Date

import io.sinq.provider.{Column, Table}
import models.h2.Zone

object T_ZONE extends Table[Zone]("t_zone") {
  def id = Column(this, classOf[Long], "id")

  def name = Column(this, classOf[String], "name")

  def createDate = Column(this, classOf[Date], "createdate")

  def p_id = Column(this, classOf[Long], "p_id")
}
