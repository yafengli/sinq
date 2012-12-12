package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.{Predicate, Order}
import scala.collection.JavaConversions._

class CriteriaQL[T](val em: EntityManager, val ct: Class[T]) {

  val builder = em.getCriteriaBuilder
  val query = builder.createQuery(ct)

  val root = query.from(ct)

  private var orders = List[Order]()
  private var predicates = List[Predicate]()

  private def select(): CriteriaQL[T] = {
    query.select(root)
    this
  }

  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    if (!orders.isEmpty) query.orderBy(orders: _*)
    if (!predicates.isEmpty) query.where(predicates: _*)

    try {
      val q = em.createQuery(query)
      if (limit > 0) q.setMaxResults(limit)
      if (offset > 0) q.setFirstResult(offset)
      q.getResultList.toList
    } catch {
      case ex: Exception => println(ex.getMessage); Nil
    }
  }

  def single(): T = {
    if (!orders.isEmpty) query.orderBy(orders: _*)
    if (!predicates.isEmpty) query.where(predicates: _*)
    em.createQuery(query).getSingleResult
  }

  def distinct(): CriteriaQL[T] = {
    query.distinct(true)
    this
  }

  def :=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates ::= builder.equal(root.get(attrName), attrVal)
    this
  }


  def :!=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates ::= builder.notEqual(root.get(attrName), attrVal)
    this
  }

  def :<:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= builder.lt(root.get(attrName), attrVal)
    this
  }

  def :>:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= builder.gt(root.get(attrName), attrVal)
    this
  }

  def :<=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= builder.le(root.get(attrName), attrVal)
    this
  }

  def :>=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= builder.ge(root.get(attrName), attrVal)
    this
  }

  def or(attrName: String, attrVal: Number): CriteriaQL[T] = {

    predicates ::= builder.ge(root.get(attrName), attrVal)
    this
  }

  def asc(attrName: String): CriteriaQL[T] = {
    orders ::= builder.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CriteriaQL[T] = {
    orders ::= builder.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates ::= builder.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates ::= builder.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CriteriaQL[T] = {
    predicates ::= builder.isNull(root.get(attrName))
    this
  }

  def isNotNull(attrName: String): CriteriaQL[T] = {
    predicates ::= builder.isNotNull(root.get(attrName))
    this
  }

  def or(f: (CriteriaQL[T]) => List[Predicate]): CriteriaQL[T] = {
    predicates ::= builder.or(f(this): _*)
    this
  }
}

object CriteriaQL {
  def apply[T](em: EntityManager, ct: Class[T]): CriteriaQL[T] = {
    new CriteriaQL[T](em, ct).select()
  }
}
