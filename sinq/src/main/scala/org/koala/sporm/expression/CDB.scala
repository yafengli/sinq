package org.koala.sporm.expression

import scala.collection.mutable

class CDB {

  import org.koala.sporm.expression.Condition._

  val linkCache = new StringBuffer()
  val paramsMap = mutable.ArrayBuffer[Any]()

  def eq(column: String, value: Any): CDB = {
    pos(value)(i => s"${column} = ?${i}")
    this
  }

  def ge(column: String, value: Any): CDB = {
    pos(value)(i => s"${column} >= ?${i}")
    this
  }

  def in(column: String, cols: Set[Any]): CDB = {
    pos(cols)(i => s"${column} in (?${i})")
    this
  }

  def and(condition: CDB): CDB = {
    //statement
    link(condition, AND)
    //parameter
    this
  }

  def or(condition: CDB): CDB = {
    //statement
    link(condition, OR)
    //parameter
    this
  }

  private def pos(values: Any*)(call: (Int) => String): Unit = {
    values.foreach(paramsMap += _)
    (paramsMap.size + 1 - values.size to paramsMap.size + 1).foreach(i => this.linkCache.append(call(i)))
  }

  private def link(condition: CDB, operation: String): Unit = {
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

object CDB {
  def apply(): CDB = {
    new CDB
  }
}

