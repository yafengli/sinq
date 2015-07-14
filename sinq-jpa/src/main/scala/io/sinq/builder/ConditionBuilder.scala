package io.sinq.builder

import io.sinq.Table
import io.sinq.expr.Condition

import scala.collection.mutable

case class ConditionBuilder(val tableMap: Map[Table[_], String] = Map()) {

  import Condition._

  def translate(c: Condition): String = {
    val buffer = new StringBuffer()
    if (c != null) {
      val params = mutable.ArrayBuffer[Any]()
      loopCondition(c, buffer, params)
    }
    buffer.toString
  }

  def params(c: Condition): Seq[Any] = {
    val buffer = new StringBuffer()
    val params = mutable.ArrayBuffer[Any]()
    if (c != null) {
      loopCondition(c, buffer, params)
    }
    params.toSeq
  }

  private def loopCondition(c: Condition, buffer: StringBuffer, params: mutable.ArrayBuffer[Any]): Unit = {
    if (c.getFrom != null) buffer.append(c.getFlag)
    if (c.getClose && c.to.size > 0) buffer.append(START_BRACKET)
    c.values.foreach(params += _)
    buffer.append(c.expression(tableMap))
    c.to.foreach(loopCondition(_, buffer, params))
    if (c.getClose && c.to.size > 0) buffer.append(END_BRACKET)
  }
}
