package io.sinq.expression

import io.sinq.rs.Column

case class Ge[T](override val column: Column, override val paramValue: T) extends Tuple1Condition[T] {
  override def link: String = ">="
}








