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

case class FromII(info: QueryInfo) {
  def from(tables: Table*): EndII = {
    info.from ++= tables
    EndII(info)
  }
}

protected case class EndII(info: QueryInfo) extends JPA {

  def where(condition: ConditionII = null): EndII = {
    if (condition != null) info.setCondition(condition)
    EndII(info)
  }

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
    if (info.select.length == 0) buffer.append("* ") else contact(info.select.toList, buffer)

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
      case head :: second :: tails =>
        buffer.append(head.name()).append(",").append(second.name())
        contact(tails, buffer)
      case last :: Nil => buffer.append(last.name())
      case Nil =>
    }
    buffer.append(" ")
  }

  def params(): List[Any] = if (info != null && info.getCondition != null && info.getCondition.params() != null) info.getCondition.params.toList else Nil

  def single(): Array[AnyRef] = withEntityManager[Array[AnyRef]] {
    em =>
      val query = em.createNativeQuery(sql())

      println(s"len:${params().length} ${params().size}")

      (0 until params().length).foreach {
        i =>
          println(s"${i}:${params()(i)}")
      }
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

  import scala.collection.JavaConversions._

  def collect[T](t: Class[T]): List[T] = withEntityManager[java.util.List[T]] {
    em =>
      val query = if (sql().indexOf("select * from") >= 0) em.createNativeQuery(sql(), t) else em.createNativeQuery(sql())
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      val result = query.getResultList
      result.asInstanceOf[java.util.List[T]]

  } getOrElse (new java.util.ArrayList()) toList
}