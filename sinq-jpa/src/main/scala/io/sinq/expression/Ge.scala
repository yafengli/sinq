package io.sinq.expression

import io.sinq.Column

case class Ge[T, K](override val column: Column[K], override val paramValue: T) extends Tuple1Condition[T] {
  override def link: String = ">="
}









