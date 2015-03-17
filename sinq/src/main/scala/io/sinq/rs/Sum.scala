package io.sinq.rs

case class Sum(val col: String) extends Column {
  override def identifier(): String = s"sum(${col})"
}
