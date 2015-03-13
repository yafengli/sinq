package io.sinq.rs

case class Count(val col: String) extends Column {
  override def name(): String = s"count(${col})"
}
