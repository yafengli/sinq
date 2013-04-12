package org.koala.sporm.jpa

import javax.persistence.criteria.Selection

/**
 * @author ya_feng_li@163.com
 * @since 1.0
 *        Criteria Query Model
 */
abstract class CQModel[T: Manifest] extends JPA {
  def getType = implicitly[Manifest[T]].runtimeClass

  implicit def generateModel(entity: T) = new BaseBuilder[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

  def fetch(call: (CQExpression[T]) => CQExpression[T]): Option[List[T]] = {
    fetch(-1, -1)(call)
  }

  def fetch(limit: Int, offset: Int)(call: (CQExpression[T]) => CQExpression[T]): Option[List[T]] = {
    withEntityManager {
      em => call(CQExpression(em, getType.asInstanceOf[Class[T]])).fetch(limit, offset)
    }
  }

  def single(call: (CQExpression[T]) => CQExpression[T]): Option[T] = {
    withEntityManager {
      em =>
        call(CQExpression(em, getType.asInstanceOf[Class[T]])).single()
    }
  }

  def count(call: (CQExpression[T]) => CQExpression[T]): Option[Long] = {
    withEntityManager {
      em =>
        call(CQExpression(em, getType.asInstanceOf[Class[T]], classOf[java.lang.Long])).count()
    }
  }

  def multi(selects: List[Selection[Any]])(call: (CQExpression[T]) => CQExpression[T]): Option[List[Any]] = {
    withEntityManager {
      em =>
        call(CQExpression(em, getType.asInstanceOf[Class[T]])).multi(selects)
    }
  }
}