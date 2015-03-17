package init

import io.sinq.SinqStream

object ImplicitsSinq {

  implicit def sinq2Count(sinq: SinqStream) = new SinqStreamExtend(sinq)
}

class SinqStreamExtend(val sinq: SinqStream) {

  def count[T](t: Class[T]): Long = {
    sinq.withEntityManager {
      em =>
        val query = em.createQuery(s"select count(t) from ${t.getName} t", classOf[java.lang.Long])
        query.getSingleResult.longValue()
    } getOrElse 0
  }
}
