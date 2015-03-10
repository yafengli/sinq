package org.koala.sporm.expression

class Le extends Condition

object Le {
  def apply(column: String, value: AnyVal): Condition = {
    val le = new Le
    val key = le.key(column)
    le.paramsMap += (key -> value)
    le.linkCache.append(s"${column} <= :${key}")
    le
  }
}
