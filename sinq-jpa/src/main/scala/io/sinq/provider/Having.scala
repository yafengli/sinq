package io.sinq.provider

import io.sinq.expression.Condition

trait Having extends Result {
  def having(c: Condition): Result
}
