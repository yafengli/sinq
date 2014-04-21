package demo.ii

import javax.persistence.EntityManager

class CriteriaProcessor(em: EntityManager) {
  def single[T](cc: CriteriaComposer[T]): Option[T] = {
    try {
      val cb = em.getCriteriaBuilder
      val cq = cb.createQuery(cc.from)
      cq.from(cc.from)
      //TODO
      //cq.where(cc._where: _*)
      Some(em.createQuery(cq).getSingleResult)
    } catch {
      case e: Exception =>
        None
    }
  }
}
