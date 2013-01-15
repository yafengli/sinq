package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.Predicate

class CriteriaQL[T](val em: EntityManager, val ct: Class[T]) extends CriteriaResult[T] {

  def currentEntityManager: EntityManager = this.em

  def findType: Class[T] = ct

  def distinct(): CriteriaQL[T] = {
    query.distinct(true)
    this
  }

  def :=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates += builder.equal(root.get(attrName), attrVal)
    this
  }


  def !=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates += builder.notEqual(root.get(attrName), attrVal)
    this
  }

  def <::(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += builder.lt(root.get(attrName), attrVal)
    this
  }

  def >::(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += builder.gt(root.get(attrName), attrVal)
    this
  }

  def <=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += builder.le(root.get(attrName), attrVal)
    this
  }

  def >=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += builder.ge(root.get(attrName), attrVal)
    this
  }

  def ||:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += builder.ge(root.get(attrName), attrVal)
    this
  }

  def asc(attrName: String): CriteriaQL[T] = {
    orders += builder.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CriteriaQL[T] = {
    orders += builder.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates += builder.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates += builder.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CriteriaQL[T] = {
    predicates += builder.isNull(root.get(attrName))
    this
  }

  def isNotNull(attrName: String): CriteriaQL[T] = {
    predicates += builder.isNotNull(root.get(attrName))
    this
  }

  @Deprecated
  def and(list: List[Predicate]): CriteriaQL[T] = {
    ::=(list)
  }

  def ::=(list: List[Predicate]): CriteriaQL[T] = {
    predicates ++= list
    this
  }
}

object CriteriaQL {
  def apply[T](em: EntityManager, ct: Class[T]): CriteriaQL[T] = {
    new CriteriaQL[T](em, ct)
  }
}
