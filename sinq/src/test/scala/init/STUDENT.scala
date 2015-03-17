package init

import io.sinq.rs.{Column, Table}

object STUDENT extends Table("t_student", "t") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def age = Column(this, "age")

  def * = Column(this, "id", "name", "age")
}
