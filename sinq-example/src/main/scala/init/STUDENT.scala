package init

import java.math.BigInteger

import io.sinq.{Column, Table}
import models.Student

object STUDENT extends Table[Student]("t_student", "s") {
  def id = Column(this, classOf[BigInteger], "id")

  def name = Column(this, classOf[String], "name")

  def age = Column(this, classOf[Int], "age")

  def teacher_id = Column(this, classOf[BigInteger], "teacher_id")

  def * = Seq(id, name, age, teacher_id)
}
