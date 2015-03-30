package io.sinq.provider

import io.sinq.Table
import io.sinq.expression.Condition

trait Join[T, K] extends InfoProvider {
  def table: Table[K]

  def on(condition: Condition): Where[T]
}

trait JoinInner[T, K] extends Join[T, K]

trait JoinLeft[T, K] extends Join[T, K]

trait JoinRight[T, K] extends Join[T, K]

trait JoinFull[T, K] extends Join[T, K]

