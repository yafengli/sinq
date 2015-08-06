package init

import java.math.BigInteger

import io.sinq.provider.{Table, Column}
import models.Family

object _FAMILY extends Table[Family]("t_family") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}
