package io.sinq.provider.jpa

import io.sinq.expr.Condition
import io.sinq.provider.{Having, QueryLink, Result}

case class HavingImpl[T](override val link: QueryLink) extends ResultImpl[T] with Having[T] {
  override def having(c: Condition): Result[T] = {
    link.setHaving(c)
    this
  }
}
