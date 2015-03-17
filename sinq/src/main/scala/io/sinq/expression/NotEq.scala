package io.sinq.expression

import io.sinq.rs.Column

case class NotEq(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.identifier()} != ?"
}
