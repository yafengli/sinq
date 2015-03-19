package io.sinq.rs

case class Sum(val col: Column) extends Column {
  override def identifier(): String = s"sum(${col.identifier()}) as ${col.identifier}"
}
