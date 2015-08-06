package io.sinq.expr

import io.sinq.provider.{Table, Column}

trait Tuple1Condition[T <: Any] extends Condition {

  def paramValue: T

  def link: String

  def suffix(tablesMap: Map[Table[_], String]): String = {
    val buffer = new StringBuffer()
    paramValue match {
      case c: Column[_] => buffer.append(c.fullName(tablesMap))
      case seq: Seq[Any] =>
        buffer.append("(")
        buffer.append(seq.map(_ => "?").mkString(","))
        buffer.append(")")
      case _ => buffer.append("?")
    }
    buffer.toString
  }

  override def expression(tablesMap: Map[Table[_], String]): String = s"${column.fullName(tablesMap)} ${link} ${suffix(tablesMap)}"

  override def values: List[Any] = paramValue match {
    case col: Column[_] => Nil
    case cols: Seq[Any] => cols.toList
    case _ => List(paramValue)
  }
}
