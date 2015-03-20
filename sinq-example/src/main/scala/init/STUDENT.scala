package init

import io.sinq.{Column, Table}
import io.sinq.rs.Column

object STUDENT extends Table("t_student", "s") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def age = Column(this, "age")

  def teacher_id = Column(this, "teacher_id")

  def * = Column(this, "id", "name", "age")
}
