package init

import io.sinq.{Column, Table}
import io.sinq.rs.Column

object USER extends Table("t_user", "u") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def age = Column(this, "age")

  def * = Seq(id, name, age)
}



