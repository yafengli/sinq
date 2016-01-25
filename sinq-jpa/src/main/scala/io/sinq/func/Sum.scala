package io.sinq.func

import io.sinq.provider.Column

case class Sum[T](val col: Column[_]) extends MethodColumn[T] {
  override def identifier(): String = "sum"
}
