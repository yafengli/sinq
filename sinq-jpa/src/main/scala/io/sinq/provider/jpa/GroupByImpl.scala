package io.sinq.provider.jpa

import io.sinq.provider._

case class GroupByImpl[T](override val link: QueryLink) extends ResultImpl[T] with GroupBy[T] {
  override def groupBy(cols: Column[_]*): Having[T] = {
    link.groupByFields ++= cols
    HavingImpl(link)
  }
}
