package models.postgres.init

import io.sinq.provider.Table
import models.postgres.User

object T_USER extends Table[User]("t_user") {
  def id = column("id", classOf[Long])

  def name = column("name", classOf[String])

  def age = column("age", classOf[Int])
}



