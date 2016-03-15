package io.sinq.provider.jpa

import io.sinq.builder.{ConditionBuilder, SqlBuilder}
import io.sinq.func.Order
import io.sinq.provider.Result

import scala.collection.JavaConversions._

abstract class ResultImpl[T] extends Result[T] {
  override def orderBy(order: Order): Result[T] = {
    link.setOrder(order)
    this
  }

  override def limit(limit: Int, offset: Int): Result[T] = {
    link.setLimit(limit, offset)
    this
  }

  override def sql(): String = SqlBuilder(link).build()

  override def params(): List[_] = {
    val cb = ConditionBuilder()
    cb.params(link.whereCondition).toList
  }

  override def single(): Option[T] = link.stream.withEntityManager[T] {
    em => //collect().headOption.getOrElse(null).asInstanceOf[T]
      val q = if (link.selectFields.size > 0) em.createNativeQuery(sql()) else em.createNativeQuery(sql(), link.fromTables.head.getType)
      (1 to params().length).foreach(i => q.setParameter(i, params()(i - 1)))
//      q.getResultList

      null.asInstanceOf[T]
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

  override def collect(): List[T] = link.stream.withEntityManager[List[T]] {
    em =>
      val q = if (link.selectFields.size > 0) em.createNativeQuery(sql()) else em.createNativeQuery(sql(), link.fromTables.head.getType)
      (1 to params().length).foreach(i => q.setParameter(i, params()(i - 1)))

      result(q.getResultList.toList)
  } getOrElse Nil
}
