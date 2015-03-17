package io.sinq.expression

import io.sinq.rs.Column

case class Eq[T](val col: Column, val paramValue: T) extends Tuple1Condition[T] {
  override def toField(): String = paramValue match {
    case c: Column => s"${col.identifier()} = ${c.identifier()}"
    case _ => s"${col.identifier()} = ?"
  }
}
