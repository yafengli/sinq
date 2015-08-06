package io.sinq.provider.jpa

import io.sinq.provider._

case class FromImpl[T](override val link: QueryLink) extends ResultImpl[T] with From[T] {
  override def from(tables: Table[_]*): Where[T] = {
    link.fromTables ++= tables
    WhereImpl(link)
  }
}
