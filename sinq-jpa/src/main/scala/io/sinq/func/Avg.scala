package io.sinq.func

import io.sinq.provider.Column

case class Avg[T](val col: Column[T]) extends MethodColumn[Int] {
  override def identifier(): String = "avg"
}
