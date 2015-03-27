package io.sinq.builder

import io.sinq.Alias
import io.sinq.provider._

trait Builder {
  def build(): String
}

case class SqlBuilder(val info: QueryInfo) extends Builder {
  override def build(): String = {
    //select
    val buffer = new StringBuffer("select ")
    if (info.selectFields.length == 0) buffer.append(info.getSelectTable.as).append(".*").append(" ") else contactAs(info.selectFields.toList, buffer)

    //from
    buffer.append("from ")
    contact(info.fromTables.toList, buffer)
    //join
    if (info.getJoin != null) {
      info.getJoin match {
        case _: JoinInner[_, _] => buffer.append(s"inner join ")
        case _: JoinLeft[_, _] => buffer.append(s"left join ")
        case _: JoinRight[_, _] => buffer.append(s"right join ")
        case _: JoinFull[_, _] => buffer.append(s"full join ")
      }
      buffer.append(s"${info.getJoin.table.identifier()} on ").append(info.on.translate()).append(" ")
    }

    //where
    if (info.whereCondition != null && info.whereCondition != null) {
      buffer.append("where ")
      buffer.append(info.whereCondition.translate())
      buffer.append(" ")
    }

    //group by
    if (info.groupByFields.length > 0) {
      buffer.append("group by ")
      contact(info.groupByFields.toList, buffer)
      if (info.getHaving != null) buffer.append("having ").append(info.getHaving.translate()).append(" ")
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

  private def contact(list: List[Alias], buffer: StringBuffer, showAlias: Boolean = false): Unit = {
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

  private def link(alias: Alias): String = {
    s"${alias.identifier()} as ${alias.as()}"
  }

  private def contactAs(list: List[Alias], buffer: StringBuffer): Unit = {
    list match {
      case Nil =>
      case last :: Nil => buffer.append(link(last)).append(" ")
      case head :: second :: Nil =>
        buffer.append(link(head)).append(",").append(link(second)).append(" ")
      case head :: second :: tails =>
        buffer.append(link(head)).append(",").append(link(second)).append(",")
        contactAs(tails, buffer)
    }
  }
}
