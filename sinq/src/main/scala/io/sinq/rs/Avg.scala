package io.sinq.rs

case class Avg(val col: String) extends Column {

  override def identifier(): String = s"avg(${col})"
}
