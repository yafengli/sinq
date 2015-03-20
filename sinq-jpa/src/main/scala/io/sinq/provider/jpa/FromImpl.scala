package io.sinq.provider.jpa

import io.sinq.Table
import io.sinq.provider._

case class FromImpl(override val info: QueryInfo) extends ResultImpl with From {
  override def from(tables: Table*): Where = {
    info.fromTables ++= tables
    WhereImpl(info)
  }
}
