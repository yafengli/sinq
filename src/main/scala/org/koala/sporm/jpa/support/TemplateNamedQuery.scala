package org.koala.sporm.jpa.support

import scala.collection.JavaConversions._
import org.koala.sporm.jpa.JPA

trait TemplateNamedQuery[T] extends JPA with Template[T] {

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

  def find(q_name: String, ops: Array[Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        try {
          val query = em.createNamedQuery(q_name, getType)
          if (offset > 0) query.setFirstResult(offset)
          if (limit > 0) query.setMaxResults(limit)

          ops.toList match {
            case Nil =>
            case list: List[Any] => for (i <- 1 to list.size) {
              query.setParameter(i, list(i - 1))
            }
          }
          query.getResultList.toList.asInstanceOf[List[T]]
        } catch {
          case e: Exception => e.printStackTrace()
          Nil
        }
    }
  }

  def find(q_name: String, ops: Array[Any]): Option[List[T]] = {
    find(q_name, ops, -1, -1)
  }

  def single(q_name: String, ops: Array[Any]): Option[T] = {
    withEntityManager {
      em =>
        val query = em.createNamedQuery(q_name, getType)
        ops.toList match {
          case Nil =>
          case list: List[Any] => for (i <- 1 to list.size) {
            query.setParameter(i, list(i - 1))
          }
        }
        query.getSingleResult.asInstanceOf[T]
    }
  }

  def count(q_name: String, ops: Array[Any]): Option[Long] = {
    withEntityManager {
      em =>
        try {
          val query = em.createNamedQuery(q_name)
          ops.toList match {
            case Nil =>
            case list: List[Any] =>
              for (i <- 1 to ops.size) {
                query.setParameter(i, ops(i - 1))
              }
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
  }

  def count(q_name: String): Option[Long] = {
    count(q_name, Array())
  }

  def multi(q_name: String, ops: Array[Any]): Option[List[Array[_]]] = {
    withEntityManager {
      em =>
        try {
          val query = em.createQuery(q_name, classOf[Array[_]])
          ops.toList match {
            case Nil =>
            case list: List[Any] =>
              for (i <- 1 to ops.size) {
                query.setParameter(i, ops(i - 1))
              }
          }
          query.getResultList.toList
        } catch {
          case e: Exception => e.printStackTrace()
          null
        }
    }
  }

  def multi(q_name: String): Option[AnyRef] = {
    multi(q_name, Array())
  }
}