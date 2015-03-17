package io.sinq.expression

import io.sinq.rs.Column

case class NotBetween(val col: Column, val paramValue1: Any, val paramValue2: Any) extends Tuple2Condition {
  override def toField(): String = s"${col.identifier()} not between ? and ?"
}
