package demo.ii

import javax.persistence.EntityManager
import javax.persistence.criteria.{Root, CriteriaQuery, CriteriaBuilder, Predicate}

class QueryExp[T: Manifest](val em: EntityManager) {
  def getType = implicitly[Manifest[T]].runtimeClass

  val cb = em.getCriteriaBuilder
  val cq = cb.createQuery(getType)
  val from = cq.from(getType)

  def where(call: (CriteriaBuilder, CriteriaQuery[_], Root[_]) => Predicate): QueryExp[T] = {
    val list = List[Predicate](call(cb, cq, from))
    cq.where(list: _*)
    this
  }

  def single(): Option[T] = {
    try {
      Some(em.createQuery(cq).getSingleResult.asInstanceOf[T])
    } catch {
      case e: Exception => None
    }
  }

  def fetch(): Option[List[T]] = {
    try {
      Some(em.createQuery(cq).getResultList.asInstanceOf[List[T]])
    } catch {
      case e: Exception => None
    }
  }
}

trait QueryCall[T] {
  def where(call: (CriteriaBuilder, CriteriaQuery[T]) => Predicate): QueryCall[T]
}