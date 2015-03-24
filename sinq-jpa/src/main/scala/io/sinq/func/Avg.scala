package io.sinq.func

import io.sinq.Column

case class Avg[T](val col: Column[T]) extends MethodColumn[Int] {

  override def identifier(): String = s"avg(${col.identifier()})"
}