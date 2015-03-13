package io.sinq.expression

import io.sinq.rs.Column

case class Ge(val col: Column, val values: Seq[Any]) extends ConditionII {
  override def alias(): String = s"${col.name()} >= ?"
}

case class Le(val col: Column, val values: Seq[Any]) extends ConditionII {
  override def alias(): String = s"${col.name()} <= ?"
}


case class Eq(val col: Column, val values: Seq[Any]) extends ConditionII {
  override def alias(): String = s"${col.name()} = ?"
}

case class In(val col: Column, val values: Seq[Any]) extends ConditionII {
  override def alias(): String = s"${col.name()} in (?)"
}