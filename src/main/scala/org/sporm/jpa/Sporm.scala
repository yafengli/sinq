package org.sporm.jpa

import scala.collection.JavaConversions._

class Sporm[T](val unitName: String) extends JPA {

  def this() = this(System.getProperty(JPA.P_U_KEY))

  def insert(entity: T): Unit = {
    withTransaction {
      _.persist(entity)
    }
  }

  def update(entity: T): Unit = {
    withTransaction {
      _.merge(entity)
    }
  }

  def delete(entity: T): Unit = {
    withTransaction {
      _.remove(entity)
    }
  }

  def get(tc: Class[T], id: Any): Option[T] = {
    withEntityManager {
      _.find(tc, id)
    }
  }

  def find(tc: Class[T], qs: String, ops: Array[Any], offset: Int, limit: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        try {
          val query = em.createQuery(qs, tc)
          if (offset > 0) query.setFirstResult(offset)
          if (limit > 0) query.setMaxResults(limit)

          ops.toList match {
            case Nil =>
            case list: List[Any] => for (i <- 1 to list.size) {
              query.setParameter(i, list(i - 1))
            }
          }
          query.getResultList.toList
        } catch {
          case e: Exception => e.printStackTrace();
          Nil
        }
    }
  }

  def find(tc: Class[T], qs: String, ops: Array[Any]): Option[List[T]] = {
    find(tc, qs, ops, -1, -1)
  }

  def find(tc: Class[T], qs: String): Option[List[T]] = {
    find(tc, qs, null, -1, -1)
  }

  def count(qs: String, ops: Array[Any]): Option[Long] = {
    withEntityManager {
      em =>
        try {
          val query = em.createQuery(qs)
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

  def count(qs: String): Option[Long] = {
    count(qs, Array())
  }
}

object Sporm {
  def apply[T](unitName: String): Sporm[T] = {
    new Sporm[T](unitName)
  }

  def apply[T](): Sporm[T] = {
    new Sporm[T]()
  }
}