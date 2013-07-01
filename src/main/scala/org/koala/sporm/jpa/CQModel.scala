package org.koala.sporm.jpa

import javax.persistence.Tuple
import javax.persistence.criteria.{Predicate, Selection, CriteriaQuery, Root, CriteriaBuilder}

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

  def fetch(call: (CriteriaQuery[T], CQExpression[T]) => Seq[Predicate]): Option[List[T]] = {
    fetch(-1, -1)(call)
  }

  def fetch(limit: Int, offset: Int)(call: (CriteriaQuery[T], CQExpression[T]) => Seq[Predicate]): Option[List[T]] = {
    withEntityManager {
      em =>
        CQBuilder(em, getType.asInstanceOf[Class[T]], getType.asInstanceOf[Class[T]]).fetch(limit, offset)(call)
    }
  }

  def single(call: (CriteriaQuery[T], CQExpression[T]) => Seq[Predicate]): Option[T] = {
    withEntityManager {
      em =>
        CQBuilder(em, getType.asInstanceOf[Class[T]], getType.asInstanceOf[Class[T]]).single(call)
    }
  }

  def count(call: (CriteriaQuery[Tuple], CQExpression[T]) => Seq[Predicate]): Option[Long] = {
    withEntityManager {
      em =>
        CQBuilder(em, getType.asInstanceOf[Class[T]], classOf[java.lang.Long]).count(call)
    }
  }

  def multi(selectCall: (CriteriaQuery[Tuple], CQExpression[T]) => Seq[Selection[_]], call: (CriteriaQuery[Tuple], CQExpression[T]) => Seq[Predicate]): Option[List[Tuple]] = {
    withEntityManager {
      em =>
        CQBuilder(em, getType.asInstanceOf[Class[T]], classOf[Tuple]).multi(selectCall, call)
    }
  }
}