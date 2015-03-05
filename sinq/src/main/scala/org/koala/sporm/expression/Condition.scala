package org.koala.sporm.expression

trait Condition {
  val linkCache = new StringBuffer()

  def and(condition: Condition): Condition = {
    this.linkCache.append(" and (").append(condition.linkCache.toString).append(") ")
    this
  }

  def or(condition: Condition): Condition = {
    this.linkCache.append(" or (").append(condition.linkCache.toString).append(") ")
    this
  }
}

