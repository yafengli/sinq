package org.koala.sporm.rs

case class Sum(val col: String) extends Column {
  override def toSql: String = s"sum(${col})"
}
