package io.sinq.expression

import io.sinq.Column

trait Tuple1Condition[T <: Any] extends Condition {

  def paramValue: T

  def link: String

  def suffix: String = {
    val buffer = new StringBuffer()
    paramValue match {
      case c: Column[_] => buffer.append(c.identifier()) //Column <-> Column
      case seq: Seq[Any] =>
        buffer.append("(")
        seq.toList match {
          case Nil =>
          case last :: Nil => buffer.append("?")
          case head :: tails => buffer.append("?"); tails.foreach(_ => buffer.append(",?"))
        }
        buffer.append(")")
      case _ => buffer.append("?")
    }
    buffer.toString
  }

  override def expression(): String = s"${column.identifier()} ${link} ${suffix}"

  override def values: List[Any] = paramValue match {
    case col: Column[_] => Nil
    case cols: Seq[Any] => cols.toList
    case _ => List(paramValue)
  }
}
