package io.sinq.expression

import io.sinq.rs.Column

case class NotIn[T](val column: Column, val paramValue: Seq[T]) extends Tuple1Condition[Seq[T]] {
  override def link: String = "not in"
}
