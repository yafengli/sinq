package io.sinq.expression

import io.sinq.rs.Column

case class Lt(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.identifier()} < ?"
}