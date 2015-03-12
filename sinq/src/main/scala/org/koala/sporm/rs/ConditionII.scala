package org.koala.sporm.rs

import scala.beans.BeanProperty
import scala.collection.mutable

trait ConditionII {
  import org.koala.sporm.rs.ConditionII._
  @BeanProperty
  var from: ConditionII = _

  @BeanProperty
  val linkCache = new StringBuffer()

  val paramsMap = mutable.Map[String, Any]()

  def and(condition: ConditionII): ConditionII = {
    condition.setFrom(this)
    //statement
    link(condition, OR)
    //parameter
    this.paramsMap ++= condition.paramsMap
    this
  }

  def or(condition: ConditionII): ConditionII = {
    condition.setFrom(this)
    //statement
    link(condition, OR)
    //parameter
    this.paramsMap ++= condition.paramsMap
    this
  }
  private def link(condition: ConditionII, operation: String): Unit = {
    this.linkCache.append(operation)
    condition.linkCache match {
      case lc if lc.indexOf(AND) >= 0 || lc.indexOf(OR) >= 0 =>
        this.linkCache.append(START_BRACKET)
        this.linkCache.append(lc.toString)
        this.linkCache.append(END_BRACKET)
      case lc => this.linkCache.append(lc)
    }
  }
}


object ConditionII {
  val AND = " and "
  val OR = " or "
  val START_BRACKET = "("
  val END_BRACKET = ")"
}

