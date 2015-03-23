package io.sinq.provider.jpa

import io.sinq.Table
import io.sinq.provider._

case class FromImpl[T](override val info: QueryInfo) extends ResultImpl[T] with From[T] {
  override def from(tables: Table[_]*): Where[T] = {
    info.fromTables ++= tables
    WhereImpl(info)
  }
}
