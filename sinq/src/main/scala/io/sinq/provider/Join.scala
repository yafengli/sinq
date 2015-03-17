package io.sinq.provider

import io.sinq.expression.Condition
import io.sinq.rs.Table

import scala.beans.BeanProperty

trait Join {
  def table: Table

  def info: QueryInfo

  @BeanProperty
  var condition: Condition = _

  def on(condition: Condition): Where = {
    this.setCondition(condition)
    info.setJoin(this)
    WhereImpl(info)
  }
}

case class JoinInner(val table: Table, val info: QueryInfo) extends Join

case class JoinLeft(val table: Table, val info: QueryInfo) extends Join

case class JoinRight(val table: Table, val info: QueryInfo) extends Join

case class JoinFull(val table: Table, val info: QueryInfo) extends Join