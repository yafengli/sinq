package io.sinq.rs

case class Count(val col: Column) extends Column {
  override def name(): String = s"count(${col.name()})"
}
