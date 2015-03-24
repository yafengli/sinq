package io.sinq.func

import java.math.BigInteger

import io.sinq.Column

case class Count[T](val col: Column[T]) extends MethodColumn[BigInteger] {
  override def identifier(): String = s"count(${col.identifier()})"
}
