package io.sinq.provider

import io.sinq.Table
import io.sinq.expression.Condition

trait Join extends InfoProvider {
  def table: Table

  def on(condition: Condition): Where
}

trait JoinInner extends Join

trait JoinLeft extends Join

trait JoinRight extends Join

trait JoinFull extends Join

