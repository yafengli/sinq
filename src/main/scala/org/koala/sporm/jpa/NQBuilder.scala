package org.koala.sporm.jpa

import javax.persistence.{Query, EntityManager}
import scala.collection.JavaConversions._

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-12
 * Time: 下午3:25
 */
trait NQBuilder {
  def _fetch[T](query: Query, pm: Map[String, Any], limit: Int, offset: Int): List[T] = {
    try {
      if (offset > 0) query.setFirstResult(offset)
      if (limit > 0) query.setMaxResults(limit)

      pm.foreach {
        p =>
          query.setParameter(p._1, p._2)
      }
      query.getResultList.toList.asInstanceOf[List[T]]
    } catch {
      case e: Exception => e.printStackTrace()
        Nil
    }
  }

  def _single[T](query: Query, pm: Map[String, Any]): T = {
    pm.foreach {
      p =>
        query.setParameter(p._1, p._2)
    }
    query.getSingleResult.asInstanceOf[T]
  }

  def _count[T](query: Query, pm: Map[String, Any]): Long = {
    try {
      pm.foreach {
        p =>
          query.setParameter(p._1, p._2)
      }
      query.getSingleResult match {
        case row: Number => row.longValue()
        case _ => 0L
      }
    } catch {
      case e: Exception => e.printStackTrace()
        0L
    }
  }

  def _multi[T](query: Query, pm: Map[String, Any]): List[Array[Any]] = {
    try {
      pm.foreach {
        p =>
          query.setParameter(p._1, p._2)
      }
      query.getResultList.toList.asInstanceOf[List[Array[Any]]]
    } catch {
      case e: Exception => e.printStackTrace()
        null
    }
  }
}
