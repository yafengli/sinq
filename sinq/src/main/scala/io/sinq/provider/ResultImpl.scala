package io.sinq.provider

import io.sinq.builder.SqlBuilder
import io.sinq.expression.Condition
import io.sinq.rs._

import scala.collection.JavaConversions._

trait Result {
  def where(condition: Condition = null): Result

  def groupBy(cols: Column*): Result

  def orderBy(order: Order): Result

  def limit(limit: Int, offset: Int): Result

  def sql(): String

  def single(): Option[Any]

  def single[T](ct: Class[T]): Option[T]

  def collect(): List[Any]

  def collect[T](t: Class[T]): List[T]
}

case class ResultImpl(info: QueryInfo) extends Result {

  def where(condition: Condition = null): ResultImpl = {
    if (condition != null) info.setCondition(condition)
    ResultImpl(info)
  }

  def groupBy(cols: Column*): ResultImpl = {
    info.groupBy ++= cols
    this
  }

  def orderBy(order: Order): ResultImpl = {
    info.setOrder(order)
    this
  }

  def limit(limit: Int, offset: Int): ResultImpl = {
    info.setLimit(limit, offset)
    this
  }

  def sql(): String = {
    SqlBuilder(info).build()
  }

  def params(): List[Any] = if (info != null && info.getCondition != null && info.getCondition.params() != null) info.getCondition.params.toList else Nil

  def single(): Option[Any] = info.stream.withEntityManager[Any] {
    em =>
      val query = em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      query.getResultList.toList match {
        case s if s == Nil || s.length != 1 => null
        case s if s.length == 1 => s(0)
      }
  }

  def single[T](ct: Class[T]): Option[T] = info.stream.withEntityManager[T] {
    em =>
      val query = em.createNativeQuery(sql(), ct)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      query.getResultList.toList match {
        case s if s == Nil || s.length != 1 => null.asInstanceOf[T]
        case s if s.length == 1 => s(0).asInstanceOf[T]
      }
  }

  def collect(): List[Any] = info.stream.withEntityManager[List[Any]] {
    em =>
      val query = em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[java.util.List[Any]].toList
  } getOrElse Nil

  def collect[T](t: Class[T]): List[T] = info.stream.withEntityManager[List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNativeQuery(sql(), t) else em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[java.util.List[T]].toList
  } getOrElse Nil
}
