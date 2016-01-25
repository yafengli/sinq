package models.postgres.init

import io.sinq.provider.{Column, Table}
import models.postgres.User

object T_USER extends Table[User]("t_user") {
  def id = Column(this, classOf[Long], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}



