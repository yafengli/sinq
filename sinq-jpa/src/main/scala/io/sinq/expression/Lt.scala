package io.sinq.expression

import io.sinq.rs.Column

case class Lt[T](val column: Column, val paramValue: T) extends Tuple1Condition[T] {
  override def link: String = "<"
}
