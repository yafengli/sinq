package io.sinq.func

import io.sinq.provider.Column

case class Count[T](val col: Column[T]) extends MethodColumn[Long] {
  override def identifier(): String = "count"
}
