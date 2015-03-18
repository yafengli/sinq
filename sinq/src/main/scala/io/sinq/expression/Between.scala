package io.sinq.expression

import io.sinq.rs.Column

case class Between[T](val column: Column, val paramValue1: T, val paramValue2: T) extends Tuple2Condition[T] {
  override def expression(): String = s"${column.identifier()} between ? and ?"
}
