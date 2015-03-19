package io.sinq.provider.impl

import io.sinq.expression.Condition
import io.sinq.provider._
import io.sinq.rs.Table

abstract class JoinImpl extends Join {
  override def on(condition: Condition): From = {
    info.setOn(condition)
    info.setJoin(this)
    FromImpl(info)
  }
}

case class JoinInnerImpl(override val table: Table, override val info: QueryInfo) extends JoinImpl with JoinInner

case class JoinLeftImpl(override val table: Table, override val info: QueryInfo) extends JoinImpl with JoinLeft

case class JoinRightImpl(override val table: Table, override val info: QueryInfo) extends JoinImpl with JoinRight

case class JoinFullImpl(override val table: Table, override val info: QueryInfo) extends JoinImpl with JoinFull