package org.koala.sporm.expression

class NotBetween extends Condition

object NotBetween {
  def apply(column: String, start: AnyRef, end: AnyRef): Condition = {
    val notBetween = new NotBetween
    val key_1 = notBetween.key(column)
    notBetween.paramsMap += (key_1 -> start)

    val key_2 = notBetween.key(column)
    notBetween.paramsMap += (key_2 -> end)
    notBetween.linkCache.append(s"${column} not between :${key_1} and :${key_2}")
    notBetween
  }
}
