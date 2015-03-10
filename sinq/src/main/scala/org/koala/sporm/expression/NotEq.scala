package org.koala.sporm.expression

class NotEq extends Condition

object NotEq {
  def apply(column: String, value: AnyRef): Condition = {
    val notEq = new NotEq
    val key = notEq.key(column)
    notEq.paramsMap += (key -> value)
    notEq.linkCache.append(s"${column} != :${key}")
    notEq
  }
}