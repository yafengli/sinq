package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.{Selection, Predicate, Order}
import scala.collection.JavaConversions._
import java.lang.Long

class CriteriaQL[T](val em: EntityManager, val ct: Class[T]) {

  val cab = em.getCriteriaBuilder
  val query = cab.createQuery(ct)
  val root = query.from(ct)

  private var orders = List[Order]()
  private var predicates = List[Predicate]()

  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    query.select(root)

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
    query.select(root)
    if (!predicates.isEmpty) query.where(predicates: _*)
    em.createQuery(query).getSingleResult
  }

  def count(): Long = {
    val countQuery = cab.createQuery(classOf[Long])
    countQuery.select(cab.count(countQuery.from(ct)))

    if (!predicates.isEmpty) countQuery.where(predicates: _*)
    em.createQuery(countQuery).getSingleResult
  }

  def multi(selects: List[Selection[_]]): List[_] = {
    val objectQuery = cab.createQuery()
    objectQuery.multiselect(selects)

    if (!predicates.isEmpty) query.where(predicates: _*)
    em.createQuery(objectQuery).getResultList.toList
  }

  def distinct(): CriteriaQL[T] = {
    query.distinct(true)
    this
  }

  def :=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates ::= cab.equal(root.get(attrName), attrVal)
    this
  }


  def !=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates ::= cab.notEqual(root.get(attrName), attrVal)
    this
  }

  def <::(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cab.lt(root.get(attrName), attrVal)
    this
  }

  def >::(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cab.gt(root.get(attrName), attrVal)
    this
  }

  def <=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cab.le(root.get(attrName), attrVal)
    this
  }

  def >=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cab.ge(root.get(attrName), attrVal)
    this
  }

  def ||:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates ::= cab.ge(root.get(attrName), attrVal)
    this
  }

  def asc(attrName: String): CriteriaQL[T] = {
    orders ::= cab.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CriteriaQL[T] = {
    orders ::= cab.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates ::= cab.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates ::= cab.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CriteriaQL[T] = {
    predicates ::= cab.isNull(root.get(attrName))
    this
  }

  def isNotNull(attrName: String): CriteriaQL[T] = {
    predicates ::= cab.isNotNull(root.get(attrName))
    this
  }

  def or(f: (CriteriaQL[T]) => List[Predicate]): CriteriaQL[T] = {
    predicates ::= cab.or(f(this): _*)
    this
  }

  def and(f: (CriteriaQL[T]) => List[Predicate]): CriteriaQL[T] = {
    predicates ::= cab.and(f(this): _*)
    this
  }
}

object CriteriaQL {
  def apply[T](em: EntityManager, ct: Class[T]): CriteriaQL[T] = {
    new CriteriaQL[T](em, ct)
  }
}
