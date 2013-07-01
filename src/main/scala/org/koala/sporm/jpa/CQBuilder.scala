package org.koala.sporm.jpa

import collection.mutable.ListBuffer
import javax.persistence.EntityManager
import javax.persistence.Tuple
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.{Predicate, Order, Selection}
import scala.collection.JavaConversions._

trait CQBuilder[T, X] {

  import javax.persistence.criteria.Root

  def currentEntityManager: EntityManager

  def from: Class[T]

  def result: Class[X]

  val builder = currentEntityManager.getCriteriaBuilder

  val criteriaQuery_t = new ThreadLocal[CriteriaQuery[T]]()
  val root_t = new ThreadLocal[Root[T]]()

  val tupleCriteriaQuery_t = new ThreadLocal[CriteriaQuery[Tuple]]()
  val tupleRoot_t = new ThreadLocal[Root[T]]()

  def criteriaQuery = {
    if (criteriaQuery_t.get() == null) criteriaQuery_t.set(builder.createQuery(from))
    criteriaQuery_t.get()
  }

  def root = {
    if (root_t.get() == null) root_t.set(criteriaQuery.from(from))
    root_t.get()
  }

  val orders = ListBuffer[Order]()
  val predicates = ListBuffer[Predicate]()

  //tuple
  def tupleCriteriaQuery = {
    if (tupleCriteriaQuery_t.get() == null) tupleCriteriaQuery_t.set(builder.createTupleQuery())
    tupleCriteriaQuery_t.get()
  }

  def tupleRoot = {
    if (tupleRoot_t.get() == null) tupleRoot_t.set(tupleCriteriaQuery.from(from))
    tupleRoot_t.get()
  }

  val tupleOrders = ListBuffer[Order]()
  val tuplePredicates = ListBuffer[Predicate]()

  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    try {
      val query = criteriaQuery
      if (!orders.isEmpty) query.orderBy(orders: _*)
      if (!predicates.isEmpty) query.where(predicates: _*)

      val q = currentEntityManager.createQuery(query)
      if (limit > 0) q.setMaxResults(limit)
      if (offset > 0) q.setFirstResult(offset)
      q.getResultList.toList
    } catch {
      case ex: Exception => ex.printStackTrace(); Nil
    }
  }

  def single(): T = {
    try {
      val query = criteriaQuery
      if (!predicates.isEmpty) query.where(predicates: _*)
      currentEntityManager.createQuery(query).getSingleResult
    } catch {
      case ex: Exception => ex.printStackTrace(); null.asInstanceOf[T]
    }
  }

  def count(): Long = {
    try {
      val query = tupleCriteriaQuery
      query.multiselect(Seq(builder.count(tupleRoot)))
      query.where(tuplePredicates.toList: _*)
      val single = currentEntityManager.createQuery(query).getSingleResult
      single.get(0).asInstanceOf[Long]
    } catch {
      case ex: Exception => ex.printStackTrace(); 0L
    }
  }

  def multi(selects: Seq[Selection[_]]): List[Tuple] = {
    try {
      val query = tupleCriteriaQuery
      query.multiselect(selects)
      if (!tuplePredicates.isEmpty) query.where(tuplePredicates: _*)
      if (!tupleOrders.isEmpty) query.orderBy(tupleOrders: _*)

      currentEntityManager.createQuery(query).getResultList.asInstanceOf[List[Tuple]]
    } catch {
      case e: Exception => e.printStackTrace(); Nil
    }
  }
}
