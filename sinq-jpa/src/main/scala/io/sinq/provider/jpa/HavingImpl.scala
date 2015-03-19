package io.sinq.provider.jpa

import io.sinq.expression.Condition
import io.sinq.provider.{Having, QueryInfo, Result}

case class HavingImpl(override val info: QueryInfo) extends ResultImpl with Having {
  override def having(c: Condition): Result = {
    info.setHaving(c)
    this
  }
}
