package org.koala.sporm

import org.koala.sporm.expression.Condition
import org.koala.sporm.jpa.JPA

import scala.collection.mutable
import scala.collection.JavaConversions._

case class SinqStream() extends JPA {
  val sql = new StringBuffer()

  val params = mutable.Map[String, Any]()

  def select(fields: String*): From = {
    sql.append("select ")
    if (fields == null) sql.append("*") else contact(fields.toList)
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
        val cb = em.getCriteriaBuilder
        val c = cb.createQuery(classOf[java.lang.Long])
        val root = c.from(t)
        c.select(cb.count(root.get("id")))
        em.createQuery(c).getSingleResult.longValue()
    } getOrElse (0)
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
    from.sinq.params ++= condition.paramsMap
    from.sinq.sql.append(condition.linkCache.toString)
    End(this)
  }
}


case class End(where: Where) extends JPA {

  def groupBy(column: String): End = {
    where.from.sinq.sql.append(s" group by ${column}")
    this
  }

  def orderBy(column: String, order: String): End = {
    where.from.sinq.sql.append(s" order by ${column} ${order}")
    this
  }

  def limit(limit: Int, offset: Int): End = {
    where.from.sinq.sql.append(s" limit ${limit} offset ${offset}")
    this
  }

  def sql(): String = where.from.sinq.sql.toString

  def params(): Map[String, Any] = where.from.sinq.params.toMap

  def single[T](): Option[T] = withEntityManager[T] {
    em =>
      val query = em.createNativeQuery(sql())
      params().foreach(t => query.setParameter(t._1, t._2))

      val result = query.getSingleResult
      result.asInstanceOf[T]
  }

  def collect[T](t: Class[T]): List[T] = withEntityManager[List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNamedQuery(sql(), t) else em.createNativeQuery(sql())
      params().foreach(t => query.setParameter(t._1, t._2))

      val result = query.getResultList
      result.asInstanceOf[List[T]]
  } getOrElse (Nil)
}
