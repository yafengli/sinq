package org.koala.sporm.rs

case class Avg(val col: String) extends Column {

  override def toSql: String = s"avg(${col})"
}
