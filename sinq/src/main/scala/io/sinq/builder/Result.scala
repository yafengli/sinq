package io.sinq.builder

import io.sinq.expression.Condition
import io.sinq.rs.{Alias, Column, Order}

import scala.collection.JavaConversions._

case class Result(info: QueryInfo) {

  def where(condition: Condition = null): Result = {
    if (condition != null) info.setCondition(condition)
    Result(info)
  }

  def groupBy(cols: Column*): Result = {
    info.groupBy ++= cols
    this
  }

  def orderBy(order: Order): Result = {
    info.setOrder(order)
    this
  }

  def limit(limit: Int, offset: Int): Result = {
    info.setLimit(limit, offset)
    this
  }

  def sql(): String = {
    val buffer = new StringBuffer("select ")
    if (info.select.length == 0) buffer.append("*").append(" ") else contact(info.select.toList, buffer)

    buffer.append("from ")
    contact(info.from.toList, buffer)

    if (info.getCondition != null) {
      buffer.append("where ")
      buffer.append(info.getCondition.translate())
      buffer.append(" ")
    }

    if (info.groupBy.length > 0) {
      buffer.append("group by ")
      contact(info.groupBy.toList, buffer)
    }

    if (info.getOrder != null) {
      buffer.append("order by ")
      contact(info.getOrder.cols.toList, buffer)
      buffer.append(info.getOrder.orderDesc.name()).append(" ")
    }

    info.getLimit match {
      case (limit, offset) => buffer.append(s"limit ${limit} offset ${offset}")
      case null =>
    }
    buffer.toString
  }

  private def contact(list: List[Alias], buffer: StringBuffer): Unit = {
    list match {
      case Nil =>
      case last :: Nil => buffer.append(last.name()).append(" ")
      case head :: second :: Nil =>
        buffer.append(head.name()).append(",").append(second.name()).append(" ")
      case head :: second :: tails =>
        buffer.append(head.name()).append(",").append(second.name()).append(",")
        contact(tails, buffer)
    }
  }

  def params(): List[Any] = if (info != null && info.getCondition != null && info.getCondition.params() != null) info.getCondition.params.toList else Nil

  def single(): Option[Any] = info.stream.withEntityManager[Any] {
    em =>
      val query = em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      if (query.getResultList.size() != 1) null else query.getSingleResult.asInstanceOf[Any]
  }

  def single[T](ct: Class[T]): Option[T] = info.stream.withEntityManager[T] {
    em =>
      val query = em.createNativeQuery(sql(), ct)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      if (query.getResultList.size() != 1) null.asInstanceOf[T] else query.getSingleResult.asInstanceOf[T]
  }

  def collect[T](t: Class[T]): List[T] = info.stream.withEntityManager[List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNativeQuery(sql(), t) else em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[java.util.List[T]].toList

  } getOrElse Nil
}
