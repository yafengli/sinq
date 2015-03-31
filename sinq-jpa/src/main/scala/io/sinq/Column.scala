package io.sinq

trait Column[+T] extends Alias {
  def table: Table[_]

  def fullName(tablesMap: Map[Table[_], String]): String = if (tablesMap != null && tablesMap.contains(table)) s"${tablesMap(table)}.${this.identifier()}" else this.identifier()

  def as(tablesMap: Map[Table[_], String]): String = s"${fullName(tablesMap)} as ${fullName(tablesMap).replaceAll( """[\.|(|)]""", "_")}"
}

object Column {
  def apply[T, K](t: Table[K], ct: Class[T], col: String): Column[T] = new Column[T] {
    override def table = t

    override def identifier(): String = s"${col}"
  }
}
