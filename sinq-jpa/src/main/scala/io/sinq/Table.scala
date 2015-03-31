package io.sinq

abstract class Table[T: Manifest](val tableName: String) extends Alias {
  def getType = implicitly[Manifest[T]].runtimeClass

  override def identifier(): String = s"${tableName}"
}
