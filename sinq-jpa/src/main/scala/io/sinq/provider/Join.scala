package io.sinq.provider

import io.sinq.expr.Condition
import io.sinq.provider.Table

trait Join[T, K] extends Aggregation {
  def table: Table[K]

  def on(condition: Condition): Where[T]
}

trait JoinInner[T, K] extends Join[T, K]

trait JoinLeft[T, K] extends Join[T, K]

trait JoinRight[T, K] extends Join[T, K]

trait JoinFull[T, K] extends Join[T, K]

