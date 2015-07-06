package init

import java.math.BigInteger

import io.sinq.{Column, Table}
import models.User

object _USER extends Table[User]("t_user") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")
}


