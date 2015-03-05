package org.koala.sporm.expression

class Ge extends Condition

object Ge {
  def apply(column: String, value: AnyVal): Condition = {
    val ge = new Ge
    ge.linkCache.append(s"${column} >= ${value}")
    ge
  }
}
