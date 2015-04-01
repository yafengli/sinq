package init

import java.math.BigInteger

import models.User
import io.sinq.{Column, Table}

object USER extends Table[User]("t_user") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")

  def * = Seq(id, name, age)
}



