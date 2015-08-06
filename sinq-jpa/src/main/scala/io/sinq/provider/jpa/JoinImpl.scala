package io.sinq.provider.jpa

import io.sinq.expr.Condition
import io.sinq.provider._

abstract class JoinImpl[T, K] extends Join[T, K] {
  override def on(condition: Condition): Where[T] = {
    link.setOn(condition)
    link.setJoin(this)
    WhereImpl[T](link)
  }
}

case class JoinInnerImpl[T, K](override val table: Table[K], override val link: QueryLink) extends JoinImpl[T, K] with JoinInner[T, K]

case class JoinLeftImpl[T, K](override val table: Table[K], override val link: QueryLink) extends JoinImpl[T, K] with JoinLeft[T, K]

case class JoinRightImpl[T, K](override val table: Table[K], override val link: QueryLink) extends JoinImpl[T, K] with JoinRight[T, K]

case class JoinFullImpl[T, K](override val table: Table[K], override val link: QueryLink) extends JoinImpl[T, K] with JoinFull[T, K]