package org.koala.sporm.expression

class Gt extends Condition

object Gt {
  def apply(column: String, value: AnyVal): Condition = {
    val gt = new Gt
    val key = gt.key(column)
    gt.paramsMap += (key -> value)
    gt.linkCache.append(s"${column} > :${key}")
    gt
  }
}
