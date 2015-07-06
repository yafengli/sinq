package init

import java.math.BigInteger

import io.sinq.{Column, Table}
import models.Family

object _FAMILY extends Table[Family]("t_family") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}
