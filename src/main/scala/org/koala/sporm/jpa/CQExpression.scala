package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.Path
import javax.persistence.criteria.{Root, CriteriaBuilder, Predicate}

class CQExpression[T, X](val em: EntityManager, val fromType: Class[T], val resultType: Class[X]) extends CQBuilder[T, X] {

  def this(em: EntityManager, ft: Class[T]) = this(em, ft, null)

  def currentEntityManager: EntityManager = this.em

  def from: Class[T] = fromType

  def result: Class[X] = resultType

  def ==(attrName: String, attrVal: Any): CQExpression[T, X] = {
    predicates += builder.equal(root.get(attrName), attrVal)
    tuplePredicates += builder.equal(tupleRoot.get(attrName), attrVal)
    this
  }


  def !=(attrName: String, attrVal: Any): CQExpression[T, X] = {
    println(f"#id:${Thread.currentThread().getId} ps:${predicates} tps:${tuplePredicates}")
    predicates += builder.notEqual(root.get(attrName), attrVal)
    tuplePredicates += builder.notEqual(tupleRoot.get(attrName), attrVal)
    this
  }

  def <<(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.lt(root.get(attrName), attrVal)
    tuplePredicates += builder.lt(tupleRoot.get(attrName), attrVal)
    this
  }

  def >>(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.gt(root.get(attrName), attrVal)
    tuplePredicates += builder.gt(tupleRoot.get(attrName), attrVal)
    this
  }

  def <=(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.le(root.get(attrName), attrVal)
    tuplePredicates += builder.le(tupleRoot.get(attrName), attrVal)
    this
  }

  def >=(attrName: String, attrVal: Number): CQExpression[T, X] = {
    predicates += builder.ge(root.get(attrName), attrVal)
    tuplePredicates += builder.ge(tupleRoot.get(attrName), attrVal)
    this
  }

  def join[Y](joinName: String, attrName: String, attrVal: Any)(call: (CriteriaBuilder, Path[Y], Any) => Predicate): CQExpression[T, X] = {
    // predicates += builder.equal(join.get(attrName),attrVal)
    predicates += call(builder, root.get(joinName), attrVal)
    tuplePredicates += call(builder, tupleRoot.get(joinName), attrVal)
    this
  }

  def asc(attrName: String): CQExpression[T, X] = {
    orders += builder.asc(root.get(attrName))
    tupleOrders += builder.asc(tupleRoot.get(attrName))
    this
  }

  def desc(attrName: String): CQExpression[T, X] = {
    orders += builder.desc(root.get(attrName))
    tupleOrders += builder.desc(tupleRoot.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CQExpression[T, X] = {
    predicates += builder.like(root.get(attrName).as(classOf[String]), attrVal)
    tuplePredicates += builder.like(tupleRoot.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CQExpression[T, X] = {
    predicates += builder.notLike(root.get(attrName).as(classOf[String]), attrVal)
    tuplePredicates += builder.notLike(tupleRoot.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CQExpression[T, X] = {
    predicates += builder.isNull(root.get(attrName))
    tuplePredicates += builder.isNull(tupleRoot.get(attrName))
    this
  }

  def isNotNull(attrName: String): CQExpression[T, X] = {
    predicates += builder.isNotNull(root.get(attrName))
    tuplePredicates += builder.isNotNull(tupleRoot.get(attrName))
    this
  }

  def ::(seq: Seq[Predicate]): CQExpression[T, X] = {
    predicates ++= seq
    tuplePredicates ++= seq
    this
  }

  def ::(predicate: Predicate): CQExpression[T, X] = {
    predicates += predicate
    tuplePredicates += predicate
    this
  }

  def ::(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T, X] = {
    predicates ++= call(builder, root)
    tuplePredicates ++= call(builder, root)
    this
  }

  def or(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T, X] = {
    predicates += builder.or(call(builder, root): _*)
    tuplePredicates += builder.or(call(builder, tupleRoot): _*)
    this
  }

  def and(call: (CriteriaBuilder, Root[T]) => Array[Predicate]): CQExpression[T, X] = {
    predicates += builder.and(call(builder, root): _*)
    tuplePredicates += builder.and(call(builder, tupleRoot): _*)
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
