package init

import io.sinq.{Column, Table}
import io.sinq.rs.Column

object TEACHER extends Table("t_teacher", "t") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def price = Column(this, "price")

  def * = Column(this, "id", "name", "price")
}
