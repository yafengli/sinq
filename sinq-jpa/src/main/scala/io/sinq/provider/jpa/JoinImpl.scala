package io.sinq.provider.jpa

import io.sinq.Table
import io.sinq.expression.Condition
import io.sinq.provider._

abstract class JoinImpl[T, K] extends Join[T, K] {
  override def on(condition: Condition): Where[T] = {
    info.setOn(condition)
    info.setJoin(this)
    WhereImpl[T](info)
  }
}

case class JoinInnerImpl[T, K](override val table: Table[K], override val info: QueryInfo) extends JoinImpl[T, K] with JoinInner[T, K]

case class JoinLeftImpl[T, K](override val table: Table[K], override val info: QueryInfo) extends JoinImpl[T, K] with JoinLeft[T, K]

case class JoinRightImpl[T, K](override val table: Table[K], override val info: QueryInfo) extends JoinImpl[T, K] with JoinRight[T, K]

case class JoinFullImpl[T, K](override val table: Table[K], override val info: QueryInfo) extends JoinImpl[T, K] with JoinFull[T, K]