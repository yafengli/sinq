package io.sinq.provider.impl

import io.sinq.provider._
import io.sinq.rs.Table

case class FromImpl(override val info: QueryInfo) extends ResultImpl with From {
  override def from(tables: Table*): Where = {
    info.fromTables ++= tables
    WhereImpl(info)
  }
}
