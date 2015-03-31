package io.sinq.builder

import io.sinq.provider._
import io.sinq.{Column, Table}
import scala.collection.mutable

trait Builder {
  def build(): String
}

case class SqlBuilder(val info: QueryInfo) extends Builder {
  override def build(): String = {
    val t_map = aliasMap()
    val cb = ConditionBuilder(t_map)
    //select
    val buffer = new StringBuffer("select ")
    if (info.selectFields.length > 0) buffer.append(linkColumns(t_map, true, info.selectFields: _*))
    else buffer.append(t_map(info.fromTables.head) + ".*")
    buffer.append(" ")

    //from
    buffer.append("from ").append(linkTables(t_map, info.fromTables: _*)).append(" ")
    //join
    if (info.getJoin != null) {
      info.getJoin match {
        case _: JoinInner[_, _] => buffer.append(s"inner join ")
        case _: JoinLeft[_, _] => buffer.append(s"left join ")
        case _: JoinRight[_, _] => buffer.append(s"right join ")
        case _: JoinFull[_, _] => buffer.append(s"full join ")
      }
      buffer.append(linkTables(t_map, info.getJoin.table)).append(s" on ").append(cb.translate(info.on)).append(" ")
    }

    //where
    if (info.whereCondition != null && info.whereCondition != null) {
      buffer.append("where ")
      buffer.append(cb.translate(info.whereCondition))
      buffer.append(" ")
    }

    //group by
    if (info.groupByFields.length > 0) {
      buffer.append("group by ").append(linkColumns(t_map, false, info.groupByFields: _*))
      if (info.getHaving != null) buffer.append("having ").append(cb.translate(info.getHaving)).append(" ")
    }
    //order by
    if (info.getOrder != null) {
      buffer.append("order by ")
      buffer.append(linkColumns(t_map, false, info.getOrder.cols: _*)).append(" ")
      buffer.append(info.getOrder.orderDesc.identifier()).append(" ")
    }
    //limit offset
    info.getLimit match {
      case (limit, offset) => buffer.append(s"limit ${limit} offset ${offset}")
      case null =>
    }
    buffer.toString
  }

  private def aliasMap(): Map[Table[_], String] = {
    val map = mutable.HashMap[Table[_], String]()
    (1 to info.fromTables.length).foreach(i => map += (info.fromTables(i - 1) -> s"t${i}"))
    if (info.getJoin != null) map += (info.getJoin.table -> s"t${info.fromTables.length + 1}")
    map.toMap
  }

  private def linkColumns(t_map: Map[Table[_], String], as: Boolean, cols: Column[_]*): String = {
    if (as) cols.map(_.as(t_map)).mkString(",") else cols.map(_.fullName(t_map)).mkString(",")
  }

  private def linkTables(t_map: Map[Table[_], String], tls: Table[_]*): String = {
    tls.map {
      t =>
        if (!t_map.contains(t)) t.identifier() else s"${t.identifier()} ${t_map(t)}"
    } mkString (",")
  }
}
