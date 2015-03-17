package io.sinq.rs

import io.sinq.expression.Condition
import io.sinq.provider.{QueryInfo, Result, ResultImpl}

import scala.beans.BeanProperty

trait Join {
  def on(condition: Condition): Result = {
    this.setCondition(condition)
    info.setJoin(this)
    ResultImpl(info)
  }

  def table: Table

  def info: QueryInfo

  @BeanProperty
  var condition: Condition = _
}

case class JoinInner(val table: Table, val info: QueryInfo) extends Join

case class JoinLeft(val table: Table, val info: QueryInfo) extends Join

case class JoinRight(val table: Table, val info: QueryInfo) extends Join

case class JoinFull(val table: Table, val info: QueryInfo) extends Join