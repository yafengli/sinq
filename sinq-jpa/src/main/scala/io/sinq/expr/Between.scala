package io.sinq.expr

import io.sinq.provider.{Table, Column}

case class Between[T, K](override val column: Column[K], override val paramValue1: T, override val paramValue2: T) extends Tuple2Condition[T] {
  override def expression(tablesMap: Map[Table[_], String]): String = s"${column.fullName(tablesMap)} between ? and ?"
}
