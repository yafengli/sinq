package org.koala.sporm.rs

case class Ge(val col: Column, val values: Seq[Any]) extends ConditionII {
  override def alias(): String = s"${col.name()} >= ?"
}

case class Le(val col: Column, val values: Seq[Any]) extends ConditionII {
  override def alias(): String = s"${col.name()} <= ?"
}
