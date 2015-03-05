package org.koala.sporm.expression

trait Condition {

  import org.koala.sporm.expression.Condition._

  val linkCache = new StringBuffer()

  def and(condition: Condition): Condition = {
    this.linkCache.append(AND)
    condition.linkCache match {
      case lc if lc.indexOf(AND) >= 0 || lc.indexOf(OR) >= 0 =>
        this.linkCache.append(START_BRACKET)
        this.linkCache.append(lc.toString)
        this.linkCache.append(END_BRACKET)
      case lc => this.linkCache.append(lc.toString)
    }
    this
  }

  def or(condition: Condition): Condition = {
    this.linkCache.append(OR)
    condition.linkCache match {
      case lc if lc.indexOf(AND) >= 0 || lc.indexOf(OR) >= 0 =>
        this.linkCache.append(START_BRACKET)
        this.linkCache.append(lc.toString)
        this.linkCache.append(END_BRACKET)
      case lc => this.linkCache.append(lc.toString)
    }
    this
  }
}

object Condition {
  val AND: String = " and "
  val OR: String = " or "
  val START_BRACKET: String = "("
  val END_BRACKET: String = ")"
}

