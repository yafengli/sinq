package io.sinq.rs

import io.sinq.expression.Condition
import io.sinq.provider.Result

trait Join {
  def on(condition: Condition): Result
}

trait JoinInner extends Join

trait JoinLeft extends Join

trait JoinRight extends Join

trait JoinFull extends Join