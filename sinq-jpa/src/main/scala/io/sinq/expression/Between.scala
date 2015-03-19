package io.sinq.expression

import io.sinq.rs.Column

case class Between[T](override val column: Column, override val paramValue1: T, override val paramValue2: T) extends Tuple2Condition[T] {
  override def expression(): String = s"${column.identifier()} between ? and ?"
}
