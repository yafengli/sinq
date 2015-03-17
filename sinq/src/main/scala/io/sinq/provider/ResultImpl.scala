package io.sinq.provider

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
    //select
    val buffer = new StringBuffer("select ")
    if (info.select.length == 0) buffer.append("*").append(" ") else contact(info.select.toList, buffer)

    //from
    buffer.append("from ")
    contact(info.from.toList, buffer)

    //join
    if (info.getJoin != null) {
      info.getJoin match {
        case JoinInner => buffer.append(s"join ")
        case JoinLeft => buffer.append(s"left join ")
        case JoinRight => buffer.append(s"right join ")
        case JoinFull => buffer.append(s"full join ")
      }
      buffer.append(s"${info.getJoin.table.identifier()} on ").append(info.getJoin.getCondition.translate()).append(" ")
    }
    
    //where
    if (info.getCondition != null) {
      buffer.append("where ")
      buffer.append(info.getCondition.translate())
      buffer.append(" ")
    }

    //group by
    if (info.groupBy.length > 0) {
      buffer.append("group by ")
      contact(info.groupBy.toList, buffer)
    }
    //order by
    if (info.getOrder != null) {
      buffer.append("order by ")
      contact(info.getOrder.cols.toList, buffer)
      buffer.append(info.getOrder.orderDesc.identifier()).append(" ")
    }
    //limit offset
    info.getLimit match {
      case (limit, offset) => buffer.append(s"limit ${limit} offset ${offset}")
      case null =>
    }
    buffer.toString
  }

  private def contact(list: List[Alias], buffer: StringBuffer): Unit = {
    list match {
      case Nil =>
      case last :: Nil => buffer.append(last.identifier()).append(" ")
      case head :: second :: Nil =>
        buffer.append(head.identifier()).append(",").append(second.identifier()).append(" ")
      case head :: second :: tails =>
        buffer.append(head.identifier()).append(",").append(second.identifier()).append(",")
        contact(tails, buffer)
    }
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
