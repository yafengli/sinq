package io.sinq.expression

import io.sinq.rs.Column

case class Between[T](val col: Column, val paramValue1: T, val paramValue2: T) extends Tuple2Condition[T] {
  override def toField(): String = s"${col.identifier()} between ? and ?"
}
