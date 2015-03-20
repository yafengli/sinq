package io.sinq.provider.jpa

import io.sinq.Column
import io.sinq.provider._

case class GroupByImpl(override val info: QueryInfo) extends ResultImpl with GroupBy {
  override def groupBy(cols: Column*): Having = {
    info.groupByFields ++= cols
    HavingImpl(info)
  }
}
