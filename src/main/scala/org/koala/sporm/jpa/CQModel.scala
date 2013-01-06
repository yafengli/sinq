package org.koala.sporm.jpa

import javax.persistence.criteria.{CriteriaBuilder, Root, Predicate, Selection}


abstract class CQModel[T: Manifest] extends JPA {
  def getType = implicitly[Manifest[T]].runtimeClass //2.10+


  //@Deprecated  def getType = implicitly[Manifest[T]].erasure //2.9.2

  implicit def generateModel(entity: T) = new BaseOperator[T](entity)


  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

  /** Criteria API */
  def fetch(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    fetch(-1, -1)(call)
  }

  def fetch(limit: Int, offset: Int)(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).fetch(limit, offset)
    }
  }

  def single(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[T] = {
    withEntityManager {
      em => call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).single()
    }
  }

  def count(call: (CriteriaBuilder, Root[T]) => List[Predicate]): Option[Long] = {
    withEntityManager {
      em =>
        val ct = getType.asInstanceOf[Class[T]]
        val cab = em.getCriteriaBuilder
        val cq = cab.createQuery(classOf[java.lang.Long])
        val root = cq.from(ct)

        cq.select(cab.count(cq.from(ct)))
        cq.where(call(cab, root): _*)
        em.createQuery(cq).getSingleResult.toLong
    }
  }

  def multi(selects: List[Selection[Any]])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[Any]] = {
    withEntityManager {
      em =>
        call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).multi(selects)
    }
  }
}