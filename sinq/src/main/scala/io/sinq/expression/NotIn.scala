package io.sinq.expression

import io.sinq.rs.Column

case class NotIn(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} not in (?)"
}
