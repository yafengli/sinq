package init

import io.sinq.rs.{Column, Table}

/**
 * Created by Administrator on 2015/3/19.
 */
object ADDRESS extends Table("t_address", "a") {
  def id = Column(this, "id")

  def name = Column(this, "name")

  def num = Column(this, "num")

  def u_id = Column(this, "u_id")

  def * = Seq(id, name, num)
}
