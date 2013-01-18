package org.koala.sporm.jpa

import scala.collection.JavaConversions._

abstract class QLModel[T: Manifest] extends JPA {
  def getType = implicitly[Manifest[T]].runtimeClass //2.10+

  //@Deprecated def getType = implicitly[Manifest[T]].erasure //2.9.2

  implicit def generateModel(entity: T) = new BaseOperator[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

  def find(qs: String, ops: Array[Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        try {
          val query = em.createQuery(qs, getType)
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

  def find(qs: String, ops: Array[Any]): Option[List[T]] = {
    find(qs, ops, -1, -1)
  }

  def single(qs: String, ops: Array[Any]): Option[T] = {
    withEntityManager {
      em =>
        val query = em.createQuery(qs, getType)
        ops.toList match {
          case Nil =>
          case list: List[Any] => for (i <- 1 to list.size) {
            query.setParameter(i, list(i - 1))
          }
        }
        query.getSingleResult.asInstanceOf[T]
    }
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

  def multi(qs: String, ops: Array[Any]): Option[List[Array[_]]] = {
    withEntityManager {
      em =>
        try {
          val query = em.createQuery(qs, classOf[Array[_]])
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

  def multi(qs: String): Option[AnyRef] = {
    multi(qs, Array())
  }
}