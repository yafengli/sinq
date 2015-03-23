package io.sinq.expression

import io.sinq.Column

case class Between[T,K](override val column: Column[K], override val paramValue1: T, override val paramValue2: T) extends Tuple2Condition[T] {
  override def expression(): String = s"${column.identifier()} between ? and ?"
}
