package init

import java.math.BigInteger

import models.Teacher
import io.sinq.{Column, Table}

object TEACHER extends Table[Teacher]("t_teacher") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def price = Column(this, classOf[Int], "price")

  def * = Seq(id, name, price)
}
