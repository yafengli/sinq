package org.koala.sporm

import org.koala.sporm.jpa.JPA
import org.koala.sporm.rs.{Column, ConditionII, SelectInfo}

class SinqIIStream extends JPA {

  def select(cols: String*): FromII = {
    val info = new SelectInfo()
    cols.foreach(info.select += Column(_))
    FromII(info)
  }
}


case class FromII(info: SelectInfo) {
  def where(): WhereII = {
    //TODO
    null
  }
}

case class WhereII(info: SelectInfo) {
  def where(condition: ConditionII): EndII = {
    if (condition != null) {
      //TODO
    }
    EndII(info)
  }
}

protected case class EndII(info: SelectInfo) extends JPA {

  def groupBy(column: String): EndII = {
    //where.from.sql.append(s" group by ${column}")
    this
  }

  def orderBy(column: String, order: String): EndII = {
    //where.from.sql.append(s" order by ${column} ${order}")
    this
  }

  def limit(limit: Int, offset: Int): EndII = {
    // where.from.sql.append(s" limit ${limit} offset ${offset}")
    this
  }

  def sql(): String = info.toString //where.from.sql.toString

  def params(): Map[String, Any] = null //where.from.params.toMap

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