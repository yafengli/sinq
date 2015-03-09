package org.koala.sporm

import org.koala.sporm.expression.Condition

import scala.collection.mutable

case class SinqStream() {
  val sql = new StringBuffer()

  val paramsMap = mutable.Map[String, Any]()

  def select(fields: String*): From = {
    sql.append("select ")
    contact(fields.toList)
    From(this)
  }

  private def contact(fields: List[String]): Unit = {
    fields match {
      case head :: first :: tail =>
        sql.append(head).append(",").append(first)
        contact(tail)
      case last :: Nil => sql.append(last)
      case Nil =>
    }
  }
}

case class From(sinq: SinqStream) {
  def from(tableName: String): Where = {
    sinq.sql.append(" from ").append(tableName)
    Where(this)
  }
}


case class Where(from: From) {

  def where(condition: Condition): End = {
    from.sinq.sql.append(" where ")
    from.sinq.paramsMap ++= condition.paramsMap
    from.sinq.sql.append(condition.linkCache.toString)
    End(this)
  }
}


case class End(where: Where) {

  def groupBy(column: String): End = {
    where.from.sinq.sql.append(s" groupBy ${column}")
    this
  }

  def orderBy(column: String, order: String): End = {
    where.from.sinq.sql.append(s" orderBy ${column} ${order}")
    this
  }

  def limit(limit: Int, offset: Int): End = {
    where.from.sinq.sql.append(s" limit ${limit} offset ${offset}")
    this
  }

  def toSql(): String = where.from.sinq.sql.toString

  def params(): Map[String, Any] = where.from.sinq.paramsMap.toMap

  def single[T](): T = null.asInstanceOf[T]

  def collect[T](): List[T] = Nil
}
