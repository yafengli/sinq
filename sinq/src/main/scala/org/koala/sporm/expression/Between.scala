package org.koala.sporm.expression

class Between extends Condition

object Between {
  def apply(column: String, start: AnyRef, end: AnyRef): Condition = {
    val between = new Between
    val key_1 = between.key(column)
    between.paramsMap += (key_1 -> start)

    val key_2 = between.key(column)
    between.paramsMap += (key_2 -> end)
    between.linkCache.append(s"${column} between :${key_1} and :${key_2}")
    between
  }
}
