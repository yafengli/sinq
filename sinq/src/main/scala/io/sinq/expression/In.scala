package io.sinq.expression

import io.sinq.rs.Column

case class In(val col: Column, val paramValue: Seq[Any]) extends Tuple1Condition {
  override def toField(): String = s"${col.identifier()} in (${split(paramValue.toList)})"

  override def values: Seq[Any] = paramValue

  private def split(list: List[Any]): String = {
    list match {
      case Nil => ""
      case last :: Nil => "?"
      case head :: tails =>
        val buffer = new StringBuffer("?")
        (0 until tails.size).foreach(t => buffer.append(",?"))
        buffer.toString
    }
  }
}
