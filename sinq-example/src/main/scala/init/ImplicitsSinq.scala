package init

import io.sinq.{SinqStream, Table}

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

  def count[T](t: Table[T]): Long = {
    sinq.withEntityManager {
      em =>
        val query = em.createNativeQuery(s"select count(0) from ${t.tableName}")
        query.getSingleResult.asInstanceOf[java.math.BigInteger].longValue()
    } getOrElse 0
  }
}
