package io.sinq

import io.sinq.provider.{JPA, From, QueryInfo}
import io.sinq.rs._

class SinqStream extends JPA {

  def select(cols: Column*): From = {
    val info = QueryInfo(this)
    info.select ++= cols
    From(info)
  }

  def find[T](id: AnyRef, t: Class[T]): Option[T] = {
    withEntityManager(_.find(t, id))
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
