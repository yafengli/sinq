package org.koala.sporm.jpa

import javax.persistence.criteria.Selection


abstract class CQModel[T: Manifest] extends JPA {
  def getType = implicitly[Manifest[T]].runtimeClass //2.10+

  //private def getType = implicitly[Manifest[T]].erasure //2.9.2

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

  def count(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[java.lang.Long] = {
    withEntityManager {
      em =>
        val ct: Class[T] = getType.asInstanceOf
        call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).count()
    }
  }

  def multi(selects: List[Selection[Any]])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[Any]] = {
    withEntityManager {
      em =>
        call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).multi(selects)
    }
  }
}