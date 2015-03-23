package io.sinq.provider.jpa

import io.sinq.Column
import io.sinq.provider._

case class GroupByImpl[T](override val info: QueryInfo) extends ResultImpl[T] with GroupBy[T] {
  override def groupBy(cols: Column[_]*): Having[T] = {
    info.groupByFields ++= cols
    HavingImpl(info)
  }
}
