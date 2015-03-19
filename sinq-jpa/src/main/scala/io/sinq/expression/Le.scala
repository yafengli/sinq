package io.sinq.expression

import io.sinq.rs.Column

case class Le[T](val column: Column, val paramValue: T) extends Tuple1Condition[T] {
  override def link: String = "<="
}
