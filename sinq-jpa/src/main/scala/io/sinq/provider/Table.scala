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

  /**
    * @param col 字段名
    * @param ct  字段类型
    * @tparam T 字段类型
    * @return 字段定义
    */
  def column[T](col: String, ct: Class[T]): Column[T] = Column(this, col, ct)
}
