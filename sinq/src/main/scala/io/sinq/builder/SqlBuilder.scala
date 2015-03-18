package io.sinq.builder

import io.sinq.provider._
import io.sinq.rs._

trait Builder {
  def build(): String
}

case class SqlBuilder(val info: QueryInfo) extends Builder {
  override def build(): String = {
    //select
    val buffer = new StringBuffer("select ")
    if (info.select.length == 0) buffer.append("*").append(" ") else contact(info.select.toList, buffer)

    //from
    buffer.append("from ")
    contact(info.from.toList, buffer)

    //join
    if (info.getJoin != null) {
      info.getJoin match {
        case JoinInner(_, _) => buffer.append(s"inner join ")
        case JoinLeft(_, _) => buffer.append(s"left join ")
        case JoinRight(_, _) => buffer.append(s"right join ")
        case JoinFull(_, _) => buffer.append(s"full join ")
      }
      buffer.append(s"${info.getJoin.table.identifier()} on ").append(info.getJoin.getCondition.translate()).append(" ")
    }

    //where
    if (info.getCondition != null) {
      buffer.append("where ")
      buffer.append(info.getCondition.translate())
      buffer.append(" ")
    }

    //group by
    if (info.groupBy.length > 0) {
      buffer.append("group by ")
      contact(info.groupBy.toList, buffer)
    }
    //order by
    if (info.getOrder != null) {
      buffer.append("order by ")
      contact(info.getOrder.cols.toList, buffer)
      buffer.append(info.getOrder.orderDesc.identifier()).append(" ")
    }
    //limit offset
    info.getLimit match {
      case (limit, offset) => buffer.append(s"limit ${limit} offset ${offset}")
      case null =>
    }
    buffer.toString
  }

  private def contact(list: List[Alias], buffer: StringBuffer): Unit = {
    list match {
      case Nil =>
      case last :: Nil => buffer.append(last.identifier()).append(" ")
      case head :: second :: Nil =>
        buffer.append(head.identifier()).append(",").append(second.identifier()).append(" ")
      case head :: second :: tails =>
        buffer.append(head.identifier()).append(",").append(second.identifier()).append(",")
        contact(tails, buffer)
    }
  }
}
