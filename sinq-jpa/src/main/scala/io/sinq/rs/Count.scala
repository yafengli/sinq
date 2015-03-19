package io.sinq.rs

case class Count(val col: Column) extends Column {
  override def identifier(): String = s"count(${col.identifier()}) as ${col.as()}"
}
