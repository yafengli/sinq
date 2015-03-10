package org.koala.sporm.expression

class Lt extends Condition

object Lt {
  def apply(column: String, value: AnyVal): Condition = {
    val lt = new Lt
    val key = lt.key(column)
    lt.paramsMap += (key -> value)
    lt.linkCache.append(s"${column} < :${key}")
    lt
  }
}