package models.h2.init

import io.sinq.provider.Table
import models.h2.Person

object T_PERSON extends Table[Person]("t_person") {
  def id = column("id", classOf[Long])

  def name = column("name", classOf[String])

  def age = column("age", classOf[Int])
}



