package io.sinq

abstract class Table(val tableName: String, override val as: String) extends Alias {
  override def identifier(): String = s"${tableName} ${as}"
}
