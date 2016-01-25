package models.h2.init

import io.sinq.provider.{Column, Table}
import models.h2.Person

object T_PERSON extends Table[Person]("t_person") {
  def id = Column(this, classOf[Long], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}



