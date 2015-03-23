package io.sinq.provider

import io.sinq.expression.Condition

trait Having[T] extends Result[T] {
  def having(c: Condition): Result[T]
}
