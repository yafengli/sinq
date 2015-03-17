package io.sinq.provider

import io.sinq.expression.Condition

trait Where {
  def where(condition: Condition = null): Result = {
    if (condition != null) info.setCondition(condition)
    ResultImpl(info)
  }

  def info: QueryInfo
}

case class WhereImpl(val info: QueryInfo) extends Where
