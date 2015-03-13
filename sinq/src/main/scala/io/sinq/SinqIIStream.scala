package io.sinq

import io.sinq.expression.ConditionII
import io.sinq.rs._
import org.koala.sporm.jpa.JPA

case class SinqIIStream() extends JPA {

  def select(cols: Column*): FromII = {
    val info = new QueryInfo()
    info.select ++= cols
    FromII(info)
  }
}


case class FromII(info: QueryInfo) {
  def from(tables: Table*): WhereII = {
    info.from ++= tables
    WhereII(info)
  }
}

case class WhereII(info: QueryInfo) {
  def where(condition: ConditionII): EndII = {
    info.setCondition(condition)
    EndII(info)
  }
}

protected case class EndII(info: QueryInfo) extends JPA {

  def groupBy(cols: Column*): EndII = {
    info.groupBy ++= cols
    this
  }

  def orderBy(order: Order): EndII = {
    info.setOrder(order)
    this
  }

  def limit(limit: Int, offset: Int): EndII = {
    info.setLimit(limit, offset)
    this
  }

  def sql(): String = {
    val buffer = new StringBuffer("select ")
    contact(info.select.toList, buffer)

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
      buffer.append(info.getOrder.order).append(" ")
    }

    info.getLimit match {
      case (limit, offset) => buffer.append(s"limit ${limit} offset ${offset}")
      case null =>
    }
    buffer.toString
  }

  private def contact(list: List[Alias], buffer: StringBuffer): Unit = {
    list match {
      case head :: second :: tails =>
        buffer.append(head.name()).append(",").append(second.name())
        contact(tails, buffer)
      case last :: Nil => buffer.append(last.name())
      case Nil =>
    }
    buffer.append(" ")
  }

  //  def params(): Map[String, Any] = info.params
  def params(): Array[Any] = info.params.toArray

  def single(): Array[AnyRef] = withEntityManager[Array[AnyRef]] {
    em =>
      val query = em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getSingleResult
      result.asInstanceOf[Array[AnyRef]]
  } getOrElse null

  def single[T](ct: Class[T]): Option[T] = withEntityManager[T] {
    em =>
      val query = em.createNativeQuery(sql(), ct)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getSingleResult
      result.asInstanceOf[T]
  }

  def collect[T](t: Class[T]): List[T] = withEntityManager[List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNativeQuery(sql(), t) else em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[List[T]]
  } getOrElse Nil
}