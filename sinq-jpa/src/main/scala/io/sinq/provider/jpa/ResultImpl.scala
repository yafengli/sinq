package io.sinq.provider.jpa

import io.sinq.builder.SqlBuilder
import io.sinq.provider.Result
import io.sinq.rs.Order

import scala.collection.JavaConversions._

abstract class ResultImpl[T] extends Result[T] {
  override def orderBy(order: Order): Result[T] = {
    info.setOrder(order)
    this
  }

  override def limit(limit: Int, offset: Int): Result[T] = {
    info.setLimit(limit, offset)
    this
  }

  override def sql(): String = SqlBuilder(info).build()

  override def params(): List[_] = if (info != null && info.whereCondition != null && info.whereCondition.params() != null) info.whereCondition.params.toList else Nil

  override def single(): Option[T] = info.stream.withEntityManager[T] {
    em =>
      val query = if (info.getSelectTable == null) em.createNativeQuery(sql()) else em.createNativeQuery(sql(), info.getSelectTable.getType)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      query.getResultList.toList match {
        case head :: Nil =>
          head match {
            case Array(_1) => (_1).asInstanceOf[T]
            case Array(_1, _2) => (_1, _2).asInstanceOf[T]
            case Array(_1, _2, _3) => (_1, _2, _3).asInstanceOf[T]
            case Array(_1, _2, _3, _4) => (_1, _2, _3, _4).asInstanceOf[T]
            case Array(_1, _2, _3, _4, _5) => (_1, _2, _3, _4, _5).asInstanceOf[T]
            case Array(_1, _2, _3, _4, _5, _6) => (_1, _2, _3, _4, _5, _6).asInstanceOf[T]

            case one: Any => head.asInstanceOf[T]
          }
        case _ => null.asInstanceOf[T]
      }
  }

  override def collect(): List[T] = info.stream.withEntityManager[List[T]] {
    em =>
      val query = if (info.getSelectTable == null) em.createNativeQuery(sql()) else em.createNativeQuery(sql(), info.getSelectTable.getType)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      query.getResultList.asInstanceOf[java.util.List[T]].toList
  } getOrElse Nil
}
