package models.h2.init

import java.util.Date

import io.sinq.provider.Table
import models.h2.Zone

object T_ZONE extends Table[Zone]("t_zone") {
  def id = column("id", classOf[Long])

  def name = column("name", classOf[String])

  def createDate = column("createdate", classOf[Date])

  def p_id = column("p_id", classOf[Long])
}
