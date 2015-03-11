package org.koala.sporm

import org.koala.sporm.expression.Condition
import org.koala.sporm.jpa.JPA

import scala.collection.mutable

case class SinqStream() extends JPA {

  def select(fields: String*): From = {
    val sql = new StringBuffer("select ")
    val params = mutable.Map[String, Any]()
    if (fields.size == 0) sql.append("*") else contact(fields.toList, sql)
    From(sql, params)
  }

  private def contact(fields: List[String], sql: StringBuffer): Unit = {
    fields match {
      case head :: first :: tail =>
        sql.append(head).append(",").append(first)
        contact(tail, sql)
      case last :: Nil => sql.append(last)
      case Nil =>
    }
  }

  def insert[T](t: T): Unit = {
    withTransaction(_.persist(t))
  }

  def delete[T](t: T): Unit = {
    withTransaction(_.remove(t))
  }

  def update[T](t: T): Unit = {
    withTransaction(_.merge(t))
  }

  def count[T](t: Class[T]): Long = {
    withEntityManager {
      em =>
        val query = em.createQuery(s"select count(t) from ${t.getName} t", classOf[java.lang.Long])
        query.getSingleResult.longValue()
    } getOrElse 0
  }
}

protected case class From(sql: StringBuffer, params: mutable.Map[String, Any]) {
  def from(tableName: String): Where = {
    sql.append(" from ").append(tableName)
    Where(this)
  }
}

protected case class Where(from: From) {

  def where(condition: Condition): End = {
    if (condition != null) {
      from.sql.append(" where ")
      from.params ++= condition.paramsMap
      from.sql.append(condition.linkCache.toString)
    }
    End(this)
  }
}

protected case class End(where: Where) extends JPA {

  def groupBy(column: String): End = {
    where.from.sql.append(s" group by ${column}")
    this
  }

  def orderBy(column: String, order: String): End = {
    where.from.sql.append(s" order by ${column} ${order}")
    this
  }

  def limit(limit: Int, offset: Int): End = {
    where.from.sql.append(s" limit ${limit} offset ${offset}")
    this
  }

  def sql(): String = where.from.sql.toString

  def params(): Map[String, Any] = where.from.params.toMap

  def single(): Array[AnyRef] = withEntityManager[Array[AnyRef]] {
    em =>
      val query = em.createNativeQuery(sql())
      params().foreach(t => query.setParameter(t._1, t._2))

      val result = query.getSingleResult
      result.asInstanceOf[Array[AnyRef]]
  } getOrElse null

  def single[T](ct: Class[T]): Option[T] = withEntityManager[T] {
    em =>
      val query = em.createNativeQuery(sql(), ct)
      params().foreach(t => query.setParameter(t._1, t._2))

      val result = query.getSingleResult
      result.asInstanceOf[T]
  }

  def collect[T](t: Class[T]): List[T] = withEntityManager[List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNativeQuery(sql(), t) else em.createNativeQuery(sql())
      params().foreach(t => query.setParameter(t._1, t._2))

      val result = query.getResultList
      result.asInstanceOf[List[T]]
  } getOrElse Nil
}
