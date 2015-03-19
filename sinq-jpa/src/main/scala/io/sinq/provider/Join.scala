package io.sinq.provider

import io.sinq.expression.Condition
import io.sinq.rs.Table

trait Join extends InfoProvider {
  def table: Table

  def on(condition: Condition): Where
}

trait JoinInner extends Join

trait JoinLeft extends Join

trait JoinRight extends Join

trait JoinFull extends Join

