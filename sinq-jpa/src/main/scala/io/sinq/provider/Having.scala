package io.sinq.provider

import io.sinq.expr.Condition

trait Having[T] extends Result[T] {
  def having(c: Condition): Result[T]
}
