package io.sinq.expression

import io.sinq.rs.Column

case class Ge(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} >= ?"
}

case class Le(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} <= ?"
}


case class Eq(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} = ?"
}

case class In(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} in (?)"
}

case class Between(val col: Column, val paramValue1: Any, val paramValue2: Any) extends Tuple2Condition {
  override def toField(): String = s"${col.name()} between ? and ?"
}
