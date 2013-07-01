package org.koala.sporm.jpa

import javax.persistence.criteria.Selection

/**
 * @author ya_feng_li@163.com
 * @since 1.0
 *        Criteria Query Model
 */
abstract class CQModel[T: Manifest] extends JPA {

  import javax.persistence.Tuple
  import javax.persistence.criteria.{Predicate, CriteriaQuery, Root, CriteriaBuilder}

  def getType = implicitly[Manifest[T]].runtimeClass

  implicit def generateModel(entity: T) = new BaseBuilder[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

  def fetch(call: (CQExpression[T, T]) => CQExpression[T, T]): Option[List[T]] = {
    fetch(-1, -1)(call)
  }

  def fetch(limit: Int, offset: Int)(call: (CQExpression[T, T]) => CQExpression[T, T]): Option[List[T]] = {
    withEntityManager {
      em => call(CQExpression(em, getType.asInstanceOf[Class[T]])).fetch(limit, offset)
    }
  }

  def single(call: (CQExpression[T, T]) => CQExpression[T, T]): Option[T] = {
    withEntityManager {
      em =>
        call(CQExpression(em, getType.asInstanceOf[Class[T]])).single()
    }
  }

  def count(call: (CQExpression[T, java.lang.Long]) => CQExpression[T, java.lang.Long]): Option[Long] = {
    withEntityManager {
      em =>
        val exp = CQExpression(em, getType.asInstanceOf[Class[T]], classOf[java.lang.Long])
        call(exp).count()
    }
  }

  def count_2(call: (CriteriaBuilder, Root[T]) => Seq[Predicate]): Option[Long] = {
    withEntityManager {
      em =>
        val exp = CQExpression(em, getType.asInstanceOf[Class[T]], classOf[java.lang.Long])
        exp.count_2(call)
    }
  }

  def multi(selectCall: (CriteriaBuilder, Root[T]) => Seq[Selection[_]])(queryCall: (CQExpression[T, Tuple]) => CQExpression[T, Tuple]): Option[List[Tuple]] = {
    withEntityManager {
      em =>
        val exp = CQExpression(em, getType.asInstanceOf[Class[T]], classOf[Tuple])
        queryCall(exp).multi(selectCall(exp.builder, exp.root))
    }
  }
}