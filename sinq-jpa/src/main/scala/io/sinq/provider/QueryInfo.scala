package io.sinq.provider

import io.sinq.expression.Condition
import io.sinq.func.Order
import io.sinq.{Column, SinqStream, Table}

import scala.beans.BeanProperty
import scala.collection.mutable

case class QueryInfo(val stream: SinqStream) {
  lazy val selectFields = mutable.ArrayBuffer[Column[_]]()
  lazy val fromTables = mutable.ArrayBuffer[Table[_]]()
  lazy val groupByFields = mutable.ArrayBuffer[Column[_]]()

  @BeanProperty
  var join: Join[_, _] = _
  @BeanProperty
  var on: Condition = _
  @BeanProperty
  var whereCondition: Condition = _
  @BeanProperty
  var from: From[_] = _
  @BeanProperty
  var result: Result[_] = _
  @BeanProperty
  var having: Condition = _
  @BeanProperty
  var limit: (Int, Int) = _
  @BeanProperty
  var order: Order = _
}





