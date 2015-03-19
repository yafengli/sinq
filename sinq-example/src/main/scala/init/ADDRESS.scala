package init

import io.sinq.rs.{Column, Table}

object ADDRESS extends Table("t_address", "a") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def num = Column(this, "num")

  def * = Seq(id, name, num)
}
