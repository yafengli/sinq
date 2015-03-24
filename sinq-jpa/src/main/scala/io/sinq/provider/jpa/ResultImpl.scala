package io.sinq.provider.jpa

import io.sinq.builder.SqlBuilder
import io.sinq.provider.Result
import io.sinq.func.Order

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

      val r = result(query.getResultList.toList).headOption
      (if(r.isEmpty) null else r.get).asInstanceOf[T]
   }

  private def result[K](list: List[K]): List[T] = {
    list.map {
      t =>
        val r = t match {
          case Array(_1) => _1
          case Array(_1, _2) => (_1, _2)
          case Array(_1, _2, _3) => (_1, _2, _3)
          case Array(_1, _2, _3, _4) => (_1, _2, _3, _4)
          case Array(_1, _2, _3, _4, _5) => (_1, _2, _3, _4, _5)
          case Array(_1, _2, _3, _4, _5, _6) => (_1, _2, _3, _4, _5, _6)
          case Array(_1, _2, _3, _4, _5, _6, _7) => (_1, _2, _3, _4, _5, _6, _7)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8) => (_1, _2, _3, _4, _5, _6, _7, _8)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9) => (_1, _2, _3, _4, _5, _6, _7, _8, _9)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20, _21) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20, _21)
          case Array(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20, _21, _22) => (_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20, _21, _22)

          case _ => t
        }
        r.asInstanceOf[T]
    }
  }

  override def collect(): List[T] = info.stream.withEntityManager[List[T]] {
    em =>
      val query = if (info.getSelectTable == null) em.createNativeQuery(sql()) else em.createNativeQuery(sql(), info.getSelectTable.getType)
      (1 to params().length).foreach(i => query.setParameter(i, params()(i - 1)))

      result(query.getResultList.toList)
  } getOrElse Nil
}
