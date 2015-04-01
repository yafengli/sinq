package init

import java.math.BigInteger
import java.util.Date

import models.Address
import io.sinq.{Column, Table}

object ADDRESS extends Table[Address]("t_address") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def createDate = Column(this, classOf[Date], "createdate")

  def num = Column(this, classOf[Int], "num")

  def u_id = Column(this, classOf[BigInteger], "u_id")

  def * = Seq(id, name, num)
}
