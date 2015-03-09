package org.koala.sporm.expression

class Ge extends Condition

object Ge {
  def apply(column: String, value: AnyVal): Condition = {
    val ge = new Ge
    val key = ge.key(column)
    ge.paramsMap += (key -> value)
    ge.linkCache.append(s"${column} >= :${key}")
    ge
  }
}
