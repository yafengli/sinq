package io.sinq.rs

case class Avg(val col: String) extends Column {

  override def name(): String = s"avg(${col})"
}
