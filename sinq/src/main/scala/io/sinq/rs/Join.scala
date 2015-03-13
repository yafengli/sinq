package io.sinq.rs

import io.sinq.WhereII
import io.sinq.expression.ConditionII

trait Join {
  def on(condition: ConditionII): WhereII
}

trait JoinInner extends Join

trait JoinLeft extends Join

trait JoinRight extends Join

trait JoinFull extends Join