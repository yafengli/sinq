package io.sinq.provider.jpa

import io.sinq.Table
import io.sinq.expr.Condition
import io.sinq.provider._

case class WhereImpl[T](override val link: QueryLink) extends ResultImpl[T] with Where[T] {
  override def where(condition: Condition): GroupBy[T] = {
    link.setWhereCondition(condition)
    GroupByImpl[T](link)
  }

  override def joinLeft[K](table: Table[K]): JoinLeft[T, K] = JoinLeftImpl[T, K](table, link)

  override def joinFull[K](table: Table[K]): JoinFull[T, K] = JoinFullImpl[T, K](table, link)

  override def join[K](table: Table[K]): JoinInner[T, K] = JoinInnerImpl[T, K](table, link)

  override def joinRight[K](table: Table[K]): JoinRight[T, K] = JoinRightImpl[T, K](table, link)
}
