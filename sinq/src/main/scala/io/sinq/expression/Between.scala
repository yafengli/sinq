package io.sinq.expression

import io.sinq.rs.Column

/**
 * Created by Administrator on 2015/3/16.
 */
case class Between(val col: Column, val paramValue1: Any, val paramValue2: Any) extends Tuple2Condition {
  override def toField(): String = s"${col.name()} between ? and ?"
}
