package org.sporm.jpa

import javax.persistence.EntityManager
import scala.collection.JavaConversions._
import javax.persistence.criteria.{Predicate, Order}


class CriteriaQL[T](val em: EntityManager, val ct: Class[T]) {

  val cb = em.getCriteriaBuilder
  val cq = cb.createQuery(ct)

  val root = cq.from(ct)

  private var orders = List[Order]()
  private var predicates = List[Predicate]()

  private def select(): CriteriaQL[T] = {
    cq.select(root)
    this
  }

  def all(): List[T] = {
    val query = em.createQuery(cq)
    query.getResultList.toList
  }

  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    if (!orders.isEmpty) cq.orderBy(orders: _*)
    if (!predicates.isEmpty) cq.where(predicates: _*)

    try {
      val query = em.createQuery(cq)
      if (limit > 0) query.setMaxResults(limit)
      if (offset > 0) query.setFirstResult(offset)
      query.getResultList.toList
    } catch {
      case ex: Exception => println(ex.getMessage); Nil
    }
  }

  def single(): Option[T] = {
    if (!orders.isEmpty) cq.orderBy(orders: _*)
    if (!predicates.isEmpty) cq.where(predicates: _*)

    try {
      Some(em.createQuery(cq).getSingleResult)
    } catch {
      case ex: Exception => println(ex.getMessage); None
    }
  }

  def :=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates ::= cb.equal(root.get(attrName), attrVal)
    this
  }


  def :!=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates ::= cb.notEqual(root.get(attrName), attrVal)
    this
  }

  def :<:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cb.lt(root.get(attrName), attrVal)
    this
  }

  def :>:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cb.gt(root.get(attrName), attrVal)
    this
  }

  def :<=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cb.le(root.get(attrName), attrVal)
    this
  }

  def :>=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cb.ge(root.get(attrName), attrVal)
    this
  }

  def or(attrName: String, attrVal: Number): CriteriaQL[T] = {

    predicates ::= cb.ge(root.get(attrName), attrVal)
    this
  }

  def asc(attrName: String): CriteriaQL[T] = {
    orders ::= cb.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CriteriaQL[T] = {
    orders ::= cb.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates ::= cb.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates ::= cb.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def or(f: (CriteriaQL[T]) => List[Predicate]): CriteriaQL[T] = {
    predicates ::= cb.or(f(this): _*)
    this
  }
}

object CriteriaQL {
  def apply[T](em: EntityManager, ct: Class[T]): CriteriaQL[T] = {
    new CriteriaQL[T](em, ct).select()
  }
}
