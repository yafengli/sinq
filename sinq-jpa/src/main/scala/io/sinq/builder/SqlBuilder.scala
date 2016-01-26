package io.sinq.builder

import io.sinq.provider._

import scala.collection.mutable

trait Builder {
  def build(): String
}

case class SqlBuilder(val ql: QueryLink) extends Builder {
  override def build(): String = {
    val tm = aliasMap() //table map
    val cb = ConditionBuilder(tm)
    //select
    val buffer = new StringBuffer("select ")
    if (ql.selectFields.length > 0) buffer.append(linkColumns(tm, true, ql.selectFields: _*))
    else buffer.append(tm(ql.fromTables.head) + ".*")
    buffer.append(" ")

    //from
    buffer.append("from ").append(linkTables(tm, ql.fromTables: _*)).append(" ")
    //join
    if (ql.getJoin != null) {
      ql.getJoin match {
        case _: JoinInner[_, _] => buffer.append(s"inner join ")
        case _: JoinLeft[_, _] => buffer.append(s"left join ")
        case _: JoinRight[_, _] => buffer.append(s"right join ")
        case _: JoinFull[_, _] => buffer.append(s"full join ")
      }
      buffer.append(linkTables(tm, ql.getJoin.table)).append(s" on ").append(cb.translate(ql.on)).append(" ")
    }

    //where
    if (ql.whereCondition != null && ql.whereCondition != null) {
      buffer.append("where ")
      buffer.append(cb.translate(ql.whereCondition))
      buffer.append(" ")
    }

    //group by
    if (ql.groupByFields.length > 0) {
      buffer.append("group by ").append(linkColumns(tm, false, ql.groupByFields: _*))
      if (ql.getHaving != null) buffer.append("having ").append(cb.translate(ql.getHaving)).append(" ")
    }
    //order by
    if (ql.getOrder != null) {
      buffer.append("order by ")
      buffer.append(linkColumns(tm, false, ql.getOrder.cols: _*)).append(" ")
      buffer.append(ql.getOrder.identifier()).append(" ")
    }
    //limit offset
    ql.getLimit match {
      case (limit, offset) => buffer.append(s"limit ${limit} offset ${offset}")
      case null =>
    }
    buffer.toString
  }

  private def aliasMap(): Map[Table[_], String] = {
    val map = mutable.HashMap[Table[_], String]()
    (1 to ql.fromTables.length).foreach(i => map += (ql.fromTables(i - 1) -> s"t${i}"))
    if (ql.getJoin != null) map += (ql.getJoin.table -> s"t${ql.fromTables.length + 1}")
    map.toMap
  }

  private def linkColumns(tm: Map[Table[_], String], as: Boolean, cols: Column[_]*): String = {
    if (as) cols.map(_.as(tm)).mkString(",") else cols.map(_.fullName(tm)).mkString(",")
  }

  private def linkTables(tm: Map[Table[_], String], tls: Table[_]*): String = {
    tls.map {
      t =>
        if (!tm.contains(t)) t.identifier() else s"${t.identifier()} ${tm(t)}"
    } mkString (",")
  }
}
