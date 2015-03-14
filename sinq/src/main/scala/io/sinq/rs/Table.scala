package io.sinq.rs

case class Table(val tableName: String, override val as: String) extends Alias {
  override def name(): String = s"${tableName} ${as}"
}
