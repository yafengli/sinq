package io.sinq

import io.sinq.provider._
import io.sinq.rs._

case class SinqStream(val persistenceName: String = "default") extends JPA {

  def select(cols: Column*): Select = {
    val info = QueryInfo(this)
    info.select ++= cols
    SelectImpl(info)
  }

  def find[T, K](id: K, t: Class[T]): Option[T] = {
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

