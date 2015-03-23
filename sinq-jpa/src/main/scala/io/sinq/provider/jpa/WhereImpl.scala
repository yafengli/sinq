package io.sinq.provider.jpa

import io.sinq.Table
import io.sinq.expression.Condition
import io.sinq.provider._

case class WhereImpl[T](override val info: QueryInfo) extends ResultImpl[T] with Where[T] {
  override def where(condition: Condition): GroupBy[T] = {
    info.setWhereCondition(condition)
    GroupByImpl[T](info)
  }

  override def joinLeft[K](table: Table[K]): JoinLeft[T, K] = JoinLeftImpl[T, K](table, info)

  override def joinFull[K](table: Table[K]): JoinFull[T, K] = JoinFullImpl[T, K](table, info)

  override def join[K](table: Table[K]): JoinInner[T, K] = JoinInnerImpl[T, K](table, info)

  override def joinRight[K](table: Table[K]): JoinRight[T, K] = JoinRightImpl[T, K](table, info)
}
