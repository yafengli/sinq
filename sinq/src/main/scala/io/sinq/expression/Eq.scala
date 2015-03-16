package io.sinq.expression

import io.sinq.rs.Column

/**
 * Created by Administrator on 2015/3/16.
 */
case class Eq(val col: Column, val paramValue: Any) extends Tuple1Condition {
  override def toField(): String = s"${col.name()} = ?"
}
