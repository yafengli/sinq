package org.koala.sporm.jpa

import collection.mutable.ListBuffer
import javax.persistence.EntityManager
import javax.persistence.Tuple
import javax.persistence.criteria.{Predicate, Root, CriteriaQuery, Order, Selection}
import scala.collection.JavaConversions._

trait CQBuilder[T, X] {

  import CQBuilder._
  import javax.persistence.criteria.CriteriaBuilder

  def currentEntityManager: EntityManager

  def from: Class[T]

  def result: Class[X]

  val builder = currentEntityManager.getCriteriaBuilder

  private val criteriaQuery_t = new ThreadLocal[CriteriaQuery[T]]()
  private val root_t = new ThreadLocal[Root[T]]()
  private val tupleCriteriaQuery_t = new ThreadLocal[CriteriaQuery[Tuple]]()
  private val tupleRoot_t = new ThreadLocal[Root[T]]()

  val orders = ListBuffer[Order]()
  val predicates = ListBuffer[Predicate]()
  val tupleOrders = ListBuffer[Order]()
  val tuplePredicates = ListBuffer[Predicate]()

  def criteriaQuery = {
    if (criteriaQuery_t.get() == null) criteriaQuery_t.set(builder.createQuery(from))
    criteriaQuery_t.get()
  }

  def root = {
    if (root_t.get() == null) root_t.set(criteriaQuery.from(from))
    root_t.get()
  }

  //tuple
  def tupleCriteriaQuery = {
    if (tupleCriteriaQuery_t.get() == null) tupleCriteriaQuery_t.set(builder.createTupleQuery())
    tupleCriteriaQuery_t.get()
  }

  def tupleRoot = {
    if (tupleRoot_t.get() == null) tupleRoot_t.set(tupleCriteriaQuery.from(from))
    tupleRoot_t.get()
  }


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
      case e: Exception => logger.error(e.getMessage); Nil
    }
  }

  def single(): T = {
    try {
      val query = criteriaQuery
      if (!predicates.isEmpty) query.where(predicates: _*)
      currentEntityManager.createQuery(query).getSingleResult
    } catch {
      case e: Exception => logger.error(e.getMessage); null.asInstanceOf[T]
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
      case e: Exception => e.printStackTrace(); 0L
    }
  }

  def count_2(call: (CriteriaBuilder, Root[T]) => Seq[Predicate]): Long = {
    try {
      val query = builder.createTupleQuery()
      val tr = query.from(from)
      query.multiselect(Seq(builder.count(tr)))
      val tps = call(builder, tr)
      query.where(tps: _*)
      val single = currentEntityManager.createQuery(query).getSingleResult
      single.get(0).asInstanceOf[Long]
    } catch {
      case e: Exception => e.printStackTrace(); 0L
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
      case e: Exception => logger.error(e.getMessage); Nil
    }
  }
}

object CQBuilder {

  import org.slf4j.LoggerFactory

  val logger = LoggerFactory.getLogger(this.getClass)
}
