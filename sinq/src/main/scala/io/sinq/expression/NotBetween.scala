package io.sinq.expression

import io.sinq.rs.Column

case class NotBetween[T](val col: Column, val paramValue1: T, val paramValue2: T) extends Tuple2Condition[T] {
  override def toField(): String = s"${col.identifier()} not between ? and ?"
}
