package demo.ii

import javax.persistence.EntityManager

class QueryExp[T](val em: EntityManager) {


  def getType = implicitly[Manifest[T]].runtimeClass

  val cb = em.getCriteriaBuilder
  val cq = cb.createQuery(getType)
  val from = cq.from(getType)

  def single(): Long = {

  }
}
