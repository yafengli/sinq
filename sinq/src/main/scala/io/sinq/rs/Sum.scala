package io.sinq.rs

case class Sum(val col: String) extends Column {
  override def name(): String = s"sum(${col})"
}
