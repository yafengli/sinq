package init

import java.math.BigInteger
import java.util.Date

import io.sinq.Table
import io.sinq.provider.{Table, Column}
import models.Address

object _ADDRESS extends Table[Address]("t_address") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def createDate = Column(this, classOf[Date], "createdate")

  def num = Column(this, classOf[Int], "num")

  def u_id = Column(this, classOf[BigInteger], "u_id")
}
