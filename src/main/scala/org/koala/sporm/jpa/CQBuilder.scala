package org.koala.sporm.jpa

import javax.persistence.criteria.{CriteriaQuery, Predicate, Order, Selection}
import javax.persistence.{criteria, EntityManager}
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._

trait CQBuilder[T] {

  val orders = ListBuffer[Order]()
  val predicates = ListBuffer[Predicate]()

  def currentEntityManager: EntityManager

  def findType: Class[T]

  def resultType: Class[_]

  val builder = currentEntityManager.getCriteriaBuilder


  val criteriaQuery = builder.createQuery(resultType)
  val root = criteriaQuery.from(findType)


  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    try {
      val query = criteriaQuery.asInstanceOf[criteria.CriteriaQuery[T]]

      query.select(root)
      if (!orders.isEmpty) query.orderBy(orders: _*)
      if (!predicates.isEmpty) query.where(predicates: _*)

      val q = currentEntityManager.createQuery(query)
      if (limit > 0) q.setMaxResults(limit)
      if (offset > 0) q.setFirstResult(offset)
      q.getResultList.toList
    } catch {
      case ex: Exception => println(ex.getMessage); Nil
    }
  }

  def single(): T = {
    try {
      val query = criteriaQuery.asInstanceOf[criteria.CriteriaQuery[T]]

      query.select(root)
      if (!predicates.isEmpty) query.where(predicates: _*)
      currentEntityManager.createQuery(query).getSingleResult
    } catch {
      case ex: Exception => null.asInstanceOf[T]
    }
  }

  def count(): Long = {
    val query = criteriaQuery.asInstanceOf[criteria.CriteriaQuery[java.lang.Long]]

    query.select(builder.count(root))
    query.where(predicates.toList: _*)
    currentEntityManager.createQuery(query).getSingleResult.toLong
  }

  def multi(selects: List[Selection[_]]): List[_] = {
    val objectQuery = builder.createQuery()
    objectQuery.multiselect(selects)

    if (!predicates.isEmpty) criteriaQuery.where(predicates: _*)
    currentEntityManager.createQuery(objectQuery).getResultList.toList
  }
}
