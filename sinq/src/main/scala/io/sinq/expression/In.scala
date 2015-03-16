package io.sinq.expression

import io.sinq.rs.Column
/**
 * Created by Administrator on 2015/3/16.
 */
case class In(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} in (?)"
}
