package io.sinq.expression

import io.sinq.rs.Column

case class In[T](val col: Column, val paramValue: Seq[T]) extends Tuple1Condition[Seq[T]] {
  override def toField(): String = s"${col.identifier()} in (${split(paramValue.toList)})"

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
