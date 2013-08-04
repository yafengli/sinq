package org.koala.sporm.jpa

import javax.persistence.EntityManager
import javax.persistence.Tuple
import javax.persistence.criteria.{Predicate, CriteriaQuery, Selection}
import scala.collection.JavaConversions._

case class CQBuilder[T, X](val em: EntityManager, val fromType: Class[T], val resultType: Class[X]) {

  import CQBuilder._

  def currentEntityManager: EntityManager = this.em

  def from: Class[T] = fromType

  def result: Class[X] = resultType

  val builder = currentEntityManager.getCriteriaBuilder

  def fetch(call: (CQExpression[T, T]) => Seq[Predicate]): List[T] = {
    fetch(-1, -1)(call)
  }

  def fetch(limit: Int, offset: Int)(call: (CQExpression[T, T]) => Seq[Predicate]): List[T] = {
    try {
      val query = builder.createQuery(from)
      val root = query.from(from)
      val ps = call(CQExpression(builder, query, root))
      if (!ps.isEmpty) query.where(ps: _*)

      val q = currentEntityManager.createQuery(query)
      if (limit > 0) q.setMaxResults(limit)
      if (offset > 0) q.setFirstResult(offset)
      q.getResultList.toList
    } catch {
      case e: Exception => e.printStackTrace(); Nil
    }
  }

  def single(call: (CQExpression[T, T]) => Seq[Predicate]): T = {
    try {
      val query = builder.createQuery(from)
      val root = query.from(from)
      val ps = call(CQExpression(builder, query, root))
      if (!ps.isEmpty) query.where(ps: _*)
      currentEntityManager.createQuery(query).getResultList.headOption match {
        case Some(o) => o
        case None => null.asInstanceOf[T]
      }
    } catch {
      case e: Exception => e.printStackTrace(); null.asInstanceOf[T]
    }
  }

  def count(call: (CQExpression[T, Tuple]) => Seq[Predicate]): Long = {
    try {
      val query = builder.createTupleQuery()
      val root = query.from(from)
      query.multiselect(Seq(builder.count(root)))
      val tps = call(CQExpression(builder, query, root))
      query.where(tps: _*)
      val single = currentEntityManager.createQuery(query).getSingleResult
      single.get(0).asInstanceOf[Long]
    } catch {
      case e: Exception => e.printStackTrace(); 0L
    }
  }

  def multi(selectsCall: (CQExpression[T, Tuple]) => Seq[Selection[_]], call: (CQExpression[T, Tuple]) => Seq[Predicate]): List[Tuple] = {
    try {
      val query = builder.createTupleQuery()
      val root = query.from(from)
      val selects = selectsCall(CQExpression(builder, query, root))
      query.multiselect(selects)
      val tps = call(CQExpression(builder, query, root))
      if (!tps.isEmpty) query.where(tps: _*)

      currentEntityManager.createQuery(query).getResultList.toList
    } catch {
      case e: Exception => e.printStackTrace(); Nil
    }
  }
}

object CQBuilder {

  import org.slf4j.LoggerFactory

  val logger = LoggerFactory.getLogger(this.getClass)
}
