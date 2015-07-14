package io.sinq.expr

import io.sinq.Column

case class Eq[T, +K](override val column: Column[K], override val paramValue: T) extends Tuple1Condition[T] {
  override def link: String = "="
}
