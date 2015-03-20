package io.sinq.expression

import io.sinq.Column

case class NotIn[T](override val column: Column, override val paramValue: Seq[T]) extends Tuple1Condition[Seq[T]] {
  override def link: String = "not in"
}
