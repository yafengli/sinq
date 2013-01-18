package org.koala.sporm.jpa

import javax.persistence.criteria.{Predicate, Order, Selection}
import javax.persistence.EntityManager
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._

trait CriteriaResult[T] {

  val orders = ListBuffer[Order]()
  val predicates = ListBuffer[Predicate]()

  def currentEntityManager: EntityManager

  def findType: Class[T]

  val builder = currentEntityManager.getCriteriaBuilder
  val query = builder.createQuery(findType)
  val root = query.from(findType)


  val countQuery = builder.createQuery(classOf[java.lang.Long])
  val countRoot = countQuery.from(findType)


  def fetch(): List[T] = {
    fetch(-1, -1)
  }

  def fetch(limit: Int, offset: Int): List[T] = {
    try {
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
      query.select(root)

      if (!predicates.isEmpty) query.where(predicates: _*)
      currentEntityManager.createQuery(query).getSingleResult
    } catch {
      case ex: Exception => null.asInstanceOf[T]
    }
  }

  def count(): Long = {
    countQuery.select(builder.count(countRoot))
    countQuery.where(predicates.toList: _*)
    currentEntityManager.createQuery(countQuery).getSingleResult.toLong
    /*
    val longQuery = builder.createQuery(classOf[java.lang.Long])
    longQuery.select(builder.count(root))
    longQuery.where(predicates.toList: _*)
    currentEntityManager.createQuery(longQuery).getSingleResult.toLong
    */
  }


  def multi(selects: List[Selection[_]]): List[_] = {

    val objectQuery = builder.createQuery()
    objectQuery.multiselect(selects)

    if (!predicates.isEmpty) query.where(predicates: _*)
    currentEntityManager.createQuery(objectQuery).getResultList.toList
  }
}
