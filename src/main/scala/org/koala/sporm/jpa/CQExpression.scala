package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.{Root, CriteriaBuilder, Expression, Predicate}
import org.hibernate.Criteria

class CQExpression[T](val em: EntityManager, val ft: Class[T], val rt: Class[_]) extends CQBuilder[T] {

  def this(em: EntityManager, ft: Class[T]) = this(em, ft, ft)

  def currentEntityManager: EntityManager = this.em

  def findType: Class[T] = ft

  def resultType: Class[_] = rt

  def ==(attrName: String, attrVal: Any): CQExpression[T] = {
    predicates += builder.equal(root.get(attrName), attrVal)
    this
  }


  def !=(attrName: String, attrVal: Any): CQExpression[T] = {
    predicates += builder.notEqual(root.get(attrName), attrVal)
    this
  }

  def <<(attrName: String, attrVal: Number): CQExpression[T] = {
    predicates += builder.lt(root.get(attrName), attrVal)
    this
  }

  def >>(attrName: String, attrVal: Number): CQExpression[T] = {
    predicates += builder.gt(root.get(attrName), attrVal)
    this
  }

  def <=(attrName: String, attrVal: Number): CQExpression[T] = {
    predicates += builder.le(root.get(attrName), attrVal)
    this
  }

  def >=(attrName: String, attrVal: Number): CQExpression[T] = {
    predicates += builder.ge(root.get(attrName), attrVal)
    this
  }

  def asc(attrName: String): CQExpression[T] = {
    orders += builder.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CQExpression[T] = {
    orders += builder.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CQExpression[T] = {
    predicates += builder.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CQExpression[T] = {
    predicates += builder.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CQExpression[T] = {
    predicates += builder.isNull(root.get(attrName))
    this
  }

  def isNotNull(attrName: String): CQExpression[T] = {
    predicates += builder.isNotNull(root.get(attrName))
    this
  }

  def ::(list: List[Predicate]): CQExpression[T] = {
    predicates ++= list
    this
  }

  def ::(seq: Predicate*): CQExpression[T] = {
    predicates ++= seq.toList
    this
  }

  def or(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T] = {
    predicates += builder.or(call(builder, root): _*)
    this
  }

  def and(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T] = {
    predicates += builder.and(call(builder, root): _*)
    this
  }
}

object CQExpression {
  def apply[T](em: EntityManager, ft: Class[T]): CQExpression[T] = {
    new CQExpression[T](em, ft)
  }

  def apply[T](em: EntityManager, ft: Class[T], rt: Class[_]): CQExpression[T] = {
    new CQExpression[T](em, ft, rt)
  }
}
