package org.koala.sporm.expression

class Eq extends Condition

object Eq {
  def apply(column: String, value: AnyRef): Condition = {
    val eq = new Eq
    val key = eq.key(column)
    eq.paramsMap += (key -> value)
    eq.linkCache.append(s"${column} = :${key}")
    eq
  }
}