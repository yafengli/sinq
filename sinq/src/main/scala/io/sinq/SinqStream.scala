package io.sinq

import io.sinq.builder.{From, QueryInfo}
import io.sinq.jpa.JPA
import io.sinq.rs._

class SinqStream extends JPA {

  def select(cols: Column*): From = {
    val info = QueryInfo(this)
    info.select ++= cols
    From(info)
  }

  def insert[T](t: T): Unit = {
    withTransaction(_.persist(t))
  }

  def delete[T](t: T): Unit = {
    withTransaction(_.remove(t))
  }

  def update[T](t: T): Unit = {
    withTransaction(_.merge(t))
  }

  def count[T](t: Class[T]): Long = {
    withEntityManager {
      em =>
        val query = em.createQuery(s"select count(t) from ${t.getName} t", classOf[java.lang.Long])
        query.getSingleResult.longValue()
    } getOrElse 0
  }
}

object SinqStream {
  def apply(pn: String): SinqStream = {
    JPA.bind(pn)
    new SinqStream()
  }

  def apply(): SinqStream = {
    new SinqStream()
  }
}
