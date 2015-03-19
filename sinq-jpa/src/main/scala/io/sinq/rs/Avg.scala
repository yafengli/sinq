package io.sinq.rs

case class Avg(val col: Column) extends Column {

  override def identifier(): String = s"avg(${col.identifier()}) as ${col.identifier()}"
}
