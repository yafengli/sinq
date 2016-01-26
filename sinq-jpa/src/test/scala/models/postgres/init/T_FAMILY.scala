package models.postgres.init

import io.sinq.provider.Table
import models.postgres.Family

object T_FAMILY extends Table[Family]("t_family") {
  def id = column("id", classOf[Long])

  def name = column("name", classOf[String])

  def age = column("age", classOf[Int])
}
