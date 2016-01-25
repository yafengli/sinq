package io.sinq.provider

trait Column[+T] extends Alias {
  /**
    * @return 字段所属表(Table).
    */
  def table: Table[_]

  /**
    * @param tablesMap 表(Table)及前缀别名.
    * @return 查询字段全称。
    */
  def fullName(tablesMap: Map[Table[_], String]): String = if (tablesMap != null && tablesMap.contains(table)) s"${tablesMap(table)}.${this.identifier()}" else this.identifier()

  /**
    * @param tablesMap 表(Table)及前缀别名.
    * @return 查询字段别名。
    */
  def as(tablesMap: Map[Table[_], String]): String = s"${fullName(tablesMap)} as ${fullName(tablesMap).replaceAll( """[\.|(]""", "_").replaceAll("""[)]""", "")}"
}

object Column {
  def apply[T, K](t: Table[K], ct: Class[T], col: String): Column[T] = new Column[T] {
    override def table = t

    override def identifier(): String = s"${col}"
  }
}
