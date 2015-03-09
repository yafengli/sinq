package org.koala.sporm

import org.koala.sporm.expression.Condition

case class SinqStream() {
  val sql = new StringBuffer()

  def select(fields: String*): From = {
    sql.append("select ")
    contact(fields.toList)
    From(sql)
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

case class From(val sql: StringBuffer) {


  def from(tableName: String): Where = {
    sql.append(" from ").append(tableName)
    Where(sql)
  }
}


case class Where(val sql: StringBuffer) {
  def where(condition: Condition): End = {
    sql.append(" where ")
    sql.append(condition.linkCache.toString)
    End(sql)
  }
}


case class End(val sql: StringBuffer) {

  def groupBy(column: String): End = {
    sql.append(s" groupBy ${column}")
    this
  }

  def orderBy(column: String, order: String): End = {
    sql.append(s" orderBy ${column} ${order}")
    this
  }

  def limit(limit: Int, offset: Int): End = {
    sql.append(s" limit ${limit} offset ${offset}")
    this
  }

  def toSql(): String = sql.toString

  def single[T](): T = null.asInstanceOf[T]

  def collect[T](): List[T] = Nil
}
