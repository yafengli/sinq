package org.koala.sporm.jpa


abstract class CQModel[T: Manifest] extends JPA {
  private def getType = implicitly[Manifest[T]].runtimeClass //2.10+

  //private def getType = implicitly[Manifest[T]].erasure //2.9.2

  implicit def generateModel(entity: T) = new BaseOperator[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id)
    }.asInstanceOf[Option[T]]
  }

  /** Criteria API */
  def fetch(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    fetch(-1, -1)(call)
  }

  def fetch(limit: Int, offset: Int)(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CriteriaQL(em, classOf[T])).fetch(limit, offset)
    }
  }

  def single(ct: Class[T])(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[T] = {
    withEntityManager {
      em => call(CriteriaQL(em, classOf[T])).single()
    }
  }

  def count(call: (CriteriaQL[T]) => CriteriaQL[T]): Option[java.lang.Long] = {
    withEntityManager {
      em =>
        call(CriteriaQL(em, classOf[T])).count()
    }
  }
}