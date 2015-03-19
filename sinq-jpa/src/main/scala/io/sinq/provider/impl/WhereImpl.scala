package io.sinq.provider.impl

import io.sinq.expression.Condition
import io.sinq.provider._
import io.sinq.rs.Table

case class WhereImpl(override val info: QueryInfo) extends ResultImpl with Where {
  override def where(condition: Condition): GroupBy = {
    info.setWhereCondition(condition)
    GroupByImpl(info)
  }

  override def joinLeft(table: Table): JoinLeft = JoinLeftImpl(table, info)

  override def joinFull(table: Table): JoinFull = JoinFullImpl(table, info)

  override def join(table: Table): JoinInner = JoinInnerImpl(table, info)

  override def joinRight(table: Table): JoinRight = JoinRightImpl(table, info)
}
