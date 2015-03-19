package io.sinq.provider.jpa

import io.sinq.provider._
import io.sinq.rs.Column

case class GroupByImpl(override val info: QueryInfo) extends ResultImpl with GroupBy {
  override def groupBy(cols: Column*): Having = {
    info.groupByFields ++= cols
    HavingImpl(info)
  }
}
