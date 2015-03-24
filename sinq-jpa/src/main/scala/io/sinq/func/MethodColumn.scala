package io.sinq.func

import io.sinq.Column

abstract class MethodColumn[T] extends Column[T] {
  def col: Column[_]

  override def as(): String = col.as()
}
