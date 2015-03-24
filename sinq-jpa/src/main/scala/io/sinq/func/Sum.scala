package io.sinq.func

import java.math.BigInteger

import io.sinq.Column

case class Sum[T](val col: Column[T]) extends MethodColumn[BigInteger] {
  override def identifier(): String = s"sum(${col.identifier()})"
}
