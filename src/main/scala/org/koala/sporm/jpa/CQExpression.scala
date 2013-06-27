package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.{Expression, Root, CriteriaBuilder, Predicate}

class CQExpression[T, X](val em: EntityManager, val ft: Class[T], val rt: Class[X]) extends CQBuilder[T] {

  import javax.persistence.criteria.{CriteriaQuery, Path}

  def this(em: EntityManager, ft: Class[T]) = this(em, ft, null)

  def currentEntityManager: EntityManager = this.em

  def findType: Class[T] = ft

  def resultType: Class[X] = rt

  def criteriaQuery[X]: CriteriaQuery[X] = {
    if (rt == null) builder.createTupleQuery.asInstanceOf[CriteriaQuery[X]]
    else builder.createQuery(resultType).asInstanceOf[CriteriaQuery[X]]
  }

  def ==(attrName: String, attrVal: Any): CQExpression[T, X] = {
    predicates += builder.equal(root.get(attrName), attrVal)
    this
  }


  def !=(attrName: String, attrVal: Any): CQExpression[T, X] = {
    predicates += builder.notEqual(root.get(attrName), attrVal)
    this
  }

  def <<(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.lt(root.get(attrName), attrVal)
    this
  }

  def >>(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.gt(root.get(attrName), attrVal)
    this
  }

  def <=(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.le(root.get(attrName), attrVal)
    this
  }

  def >=(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.ge(root.get(attrName), attrVal)
    this
  }

  def join[Y](joinName: String, attrName: String, attrVal: Any)(call: (CriteriaBuilder, Path[Y], Any) => Predicate): CQExpression[T, X] = {
    // predicates += builder.equal(join.get(attrName),attrVal)
    predicates += call(builder, root.get(joinName), attrVal)
    this
  }

  def asc(attrName: String): CQExpression[T, X] = {
    orders += builder.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CQExpression[T, X] = {
    orders += builder.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CQExpression[T, X] = {
    predicates += builder.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CQExpression[T, X] = {
    predicates += builder.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CQExpression[T, X] = {
    predicates += builder.isNull(root.get(attrName))
    this
  }

  def isNotNull(attrName: String): CQExpression[T, X] = {
    predicates += builder.isNotNull(root.get(attrName))
    this
  }

  def ::(seq: Seq[Predicate]): CQExpression[T, X] = {
    predicates ++= seq
    this
  }

  def ::(predicate: Predicate): CQExpression[T, X] = {
    predicates += predicate
    this
  }

  def ::(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T, X] = {
    predicates ++= call(builder, root)
    this
  }

  def or(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T, X] = {
    predicates += builder.or(call(builder, root): _*)
    this
  }

  def and(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T, X] = {
    predicates += builder.and(call(builder, root): _*)
    this
  }
}

object CQExpression {
  def apply[T, X](em: EntityManager, ft: Class[T]): CQExpression[T, X] = {
    new CQExpression[T, X](em, ft)
  }

  def apply[T, X](em: EntityManager, ft: Class[T], rt: Class[X]): CQExpression[T, X] = {
    new CQExpression[T, X](em, ft, rt)
  }
}
