package org.sporm.jpa

import scala.collection.JavaConversions._

class BaseService[T](val entity: T) extends JPA {

  def insert() {
    withTransaction {
      _.persist(entity)
    }
  }

  def update() {
    withTransaction {
      em =>
        em.merge(entity)
    }
  }

  def delete() {
    withTransaction {
      em =>
        em.remove(em.merge(entity))
    }
  }
}

abstract class JPAModel[T: Manifest] extends JPA {
  def getType = implicitly[Manifest[T]].runtimeClass //2.10+

  //  def getType = implicitly[Manifest[T]].erasure //2.9.2

  implicit def generateModel(entity: T) = new BaseService(entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id)
    }.asInstanceOf[Option[T]]
  }

  def find(qs: String, ops: Array[Any], offset: Int, limit: Int, order: String, desc: String): Option[List[T]] = {
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


  def find(qs: String, ops: Array[Any], offset: Int, limit: Int): Option[List[T]] = {
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

  def findAll(): Option[List[T]] = {
    find("select t from %s t".format(getType.getCanonicalName), Array(), -1, -1)
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