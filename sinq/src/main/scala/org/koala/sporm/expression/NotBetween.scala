package org.koala.sporm.expression

class NotBetween extends Condition

object NotBetween {
  def apply(column: String, start: AnyRef, end: AnyRef): Condition = {
    val notEq = new NotEq
    val key_1 = notEq.key(column)
    notEq.paramsMap += (key_1 -> start)

    val key_2 = notEq.key(column)
    notEq.paramsMap += (key_2 -> end)
    notEq.linkCache.append(s"${column} not between :${key_1} and :${key_2}")
    notEq
  }
}
