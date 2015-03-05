package org.koala.sporm.expression

class Eq extends Condition

object Eq {
  def apply(column: String, value: AnyRef): Condition = {
    val eq = new Eq
    eq.linkCache.append(s"${column} = ${value}")
    eq
  }
}