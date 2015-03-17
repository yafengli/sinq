package init

import io.sinq.rs.{Column, Table}

object BOOK extends Table("t_book", "b") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def price = Column(this, "price")

  def student_id = Column(this, "student_id")

  def * = Column(this, "id", "name", "price")
}
