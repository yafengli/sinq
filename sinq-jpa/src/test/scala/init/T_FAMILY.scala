package init

import java.math.BigInteger

import io.sinq.provider.{Table, Column}
import models.Family

object T_FAMILY extends Table[Family]("t_family") {
  def id = Column(this, classOf[Long], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}
