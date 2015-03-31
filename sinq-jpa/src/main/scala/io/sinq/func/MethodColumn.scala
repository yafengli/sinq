package io.sinq.func

import io.sinq.{Table, Column}

abstract class MethodColumn[T] extends Column[T] {

  override def table: Table[_] = col.table

  override def fullName(tablesMap: Map[Table[_], String]): String = s"${this.identifier()}(${col.fullName(tablesMap)})"

  def col: Column[_]
}
