package io.sinq.expression

import io.sinq.rs.Column

case class NotIn[T](val col: Column, val paramValue: Seq[T]) extends Tuple1Condition[Seq[T]] {
  override def toField(): String = s"${col.identifier()} not in (?)"
}
