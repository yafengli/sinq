package org.koala.sporm.jpa

import scala.collection.JavaConversions._

abstract class JPQLModel[T: Manifest] extends JPA {
  private def getType = implicitly[Manifest[T]].runtimeClass //2.10+

  //private def getType = implicitly[Manifest[T]].erasure //2.9.2

  implicit def generateModel(entity: T) = new BaseModelOpts[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id)
    }.asInstanceOf[Option[T]]
  }

  /** QL API */
  def find(qs: String, ops: Array[Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        try {
          val query = em.createQuery(qs)
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

  /** Criteria API */
  def fetch(ct: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    fetch(ct, -1, -1)(call)
  }

  def fetch(ct: Class[T], limit: Int, offset: Int)(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CriteriaQL(em, ct)).fetch(limit, offset)
    }
  }

  def single(ct: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[T] = {
    withEntityManager {
      em => call(CriteriaQL(em, ct)).single()
    }
  }
}