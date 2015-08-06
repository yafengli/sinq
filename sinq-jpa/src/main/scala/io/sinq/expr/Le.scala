package io.sinq.expr

import io.sinq.provider.Column

case class Le[T, +K](override val column: Column[K], override val paramValue: T) extends Tuple1Condition[T] {
  override def link: String = "<="
}
