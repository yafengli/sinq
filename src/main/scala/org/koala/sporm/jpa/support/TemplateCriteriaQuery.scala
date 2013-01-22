package org.koala.sporm.jpa.support

import javax.persistence.criteria.Selection
import org.koala.sporm.jpa.{JPA}


trait TemplateCriteriaQuery[T] extends JPA {
  def getType: Class[_]

  implicit def generateModel(entity: T) = new BaseOperator[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

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
      em =>
        call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).single()
    }
  }

  def count(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[Long] = {
    withEntityManager {
      em =>
        call(CriteriaQL(em, getType.asInstanceOf[Class[T]], classOf[java.lang.Long])).count()
    }
  }

  def multi(selects: List[Selection[Any]])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[Any]] = {
    withEntityManager {
      em =>
        call(CriteriaQL(em, getType.asInstanceOf[Class[T]])).multi(selects)
    }
  }
}