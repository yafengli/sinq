package io.sinq.provider.jpa

import io.sinq.builder.SqlBuilder
import io.sinq.provider.Result
import io.sinq.rs.Order

import scala.collection.JavaConversions._

abstract class ResultImpl extends Result {
  override def orderBy(order: Order): Result = {
    info.setOrder(order)
    this
  }

  override def limit(limit: Int, offset: Int): Result = {
    info.setLimit(limit, offset)
    this
  }

  override def sql(): String = SqlBuilder(info).build()

  override def params(): List[Any] = if (info != null && info.whereCondition != null && info.whereCondition.params() != null) info.whereCondition.params.toList else Nil

  override def single(): Option[Any] = info.stream.withEntityManager[Any] {
    em =>
      val query = em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      query.getResultList.toList match {
        case s if s == Nil || s.length != 1 => null
        case s if s.length == 1 => s(0)
      }
  }

  override def single[T](ct: Class[T]): Option[T] = info.stream.withEntityManager[T] {
    em =>
      val query = em.createNativeQuery(sql(), ct)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      query.getResultList.toList match {
        case s if s == Nil || s.length != 1 => null.asInstanceOf[T]
        case s if s.length == 1 => s(0).asInstanceOf[T]
      }
  }

  override def collect(): List[Any] = info.stream.withEntityManager[List[Any]] {
    em =>
      val query = em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[java.util.List[Any]].toList
  } getOrElse Nil

  override def collect[T](t: Class[T]): List[T] = info.stream.withEntityManager[List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNativeQuery(sql(), t) else em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[java.util.List[T]].toList
  } getOrElse Nil
}
