package io.sinq.provider

abstract class Table[T: Manifest](val tableName: String) extends Alias {
  /**
   * @return JPA Entity类型。
   */
  def getType = implicitly[Manifest[T]].runtimeClass

  /**
   * @return 表名.
   */
  override def identifier(): String = s"${tableName}"
}
