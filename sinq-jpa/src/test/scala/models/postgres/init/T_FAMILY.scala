package models.postgres.init

import io.sinq.provider.{Column, Table}
import models.postgres.Family

object T_FAMILY extends Table[Family]("t_family") {
  def id = Column(this, classOf[Long], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}
