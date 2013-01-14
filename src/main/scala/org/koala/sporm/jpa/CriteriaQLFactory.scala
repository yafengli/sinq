package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.criteria.{Selection, Predicate, Order}
import scala.collection.JavaConversions._
import collection.mutable.ListBuffer

class CriteriaQL[T](val em: EntityManager, val ct: Class[T]) {

  val cab = em.getCriteriaBuilder
  val query = cab.createQuery(ct)
  val root = query.from(ct)


  private var orders = ListBuffer[Order]()
  private var predicates = ListBuffer[Predicate]()

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
    try {
      query.select(root)

      if (!predicates.isEmpty) query.where(predicates: _*)
      em.createQuery(query).getSingleResult
    } catch {
      case ex: Exception => null.asInstanceOf[T]
    }
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
    predicates += cab.equal(root.get(attrName), attrVal)
    this
  }


  def !=:(attrName: String, attrVal: Any): CriteriaQL[T] = {
    predicates += cab.notEqual(root.get(attrName), attrVal)
    this
  }

  def <::(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += cab.lt(root.get(attrName), attrVal)
    this
  }

  def >::(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += cab.gt(root.get(attrName), attrVal)
    this
  }

  def <=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += cab.le(root.get(attrName), attrVal)
    this
  }

  def >=:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += cab.ge(root.get(attrName), attrVal)
    this
  }

  def ||:(attrName: String, attrVal: Number): CriteriaQL[T] = {
    predicates += cab.ge(root.get(attrName), attrVal)
    this
  }

  def asc(attrName: String): CriteriaQL[T] = {
    orders += cab.asc(root.get(attrName))
    this
  }

  def desc(attrName: String): CriteriaQL[T] = {
    orders += cab.desc(root.get(attrName))
    this
  }

  def like(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates += cab.like(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def notLike(attrName: String, attrVal: String): CriteriaQL[T] = {
    predicates += cab.notLike(root.get(attrName).as(classOf[String]), attrVal)
    this
  }

  def isNull(attrName: String): CriteriaQL[T] = {
    predicates += cab.isNull(root.get(attrName))
    this
  }

  def isNotNull(attrName: String): CriteriaQL[T] = {
    predicates += cab.isNotNull(root.get(attrName))
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
