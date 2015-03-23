package io.sinq.provider.jpa

import io.sinq.expression.Condition
import io.sinq.provider.{Having, QueryInfo, Result}

case class HavingImpl[T](override val info: QueryInfo) extends ResultImpl[T] with Having[T] {
  override def having(c: Condition): Result[T] = {
    info.setHaving(c)
    this
  }
}
