package org.koala.sporm.jpa

import collection.mutable.ListBuffer
import javax.persistence.EntityManager
import javax.persistence.Tuple
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.{Predicate, Order, Selection}
import scala.collection.JavaConversions._

trait CQBuilder[T] {

  def currentEntityManager: EntityManager

  def findType: Class[T]

  def resultType: Class[_]

  def criteriaQuery[X]: CriteriaQuery[X]

  val orders = ListBuffer[Order]()
  val predicates = ListBuffer[Predicate]()
  val builder = currentEntityManager.getCriteriaBuilder
  val root = criteriaQuery.from(findType)

  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    try {
      val query = criteriaQuery[T]

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
      val query = criteriaQuery[T]

      query.select(root)
      if (!predicates.isEmpty) query.where(predicates: _*)
      currentEntityManager.createQuery(query).getSingleResult
    } catch {
      case ex: Exception => null.asInstanceOf[T]
    }
  }

  def count(): Long = {
    try {
      val query = criteriaQuery[java.lang.Long]

      query.select(builder.count(root))
      query.where(predicates.toList: _*)
      currentEntityManager.createQuery(query).getSingleResult.toLong
    } catch {
      case ex: Exception => 0L
    }
  }

  def multi(selects: Seq[Selection[_]]): List[Tuple] = {
    try {
      val query = criteriaQuery[Tuple]
      query.multiselect(selects)
      if (!predicates.isEmpty) query.where(predicates: _*)
      currentEntityManager.createQuery(query).getResultList.toList
    } catch {
      case e: Exception => e.printStackTrace(); Nil
    }
  }
}
