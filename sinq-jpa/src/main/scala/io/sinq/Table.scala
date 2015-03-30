package io.sinq

import scala.beans.BeanProperty

abstract class Table[T: Manifest](val tableName: String) extends Alias {

  @BeanProperty var as: String = _

  def getType = implicitly[Manifest[T]].runtimeClass

  override def identifier(): String = s"${tableName} ${as}"
}
