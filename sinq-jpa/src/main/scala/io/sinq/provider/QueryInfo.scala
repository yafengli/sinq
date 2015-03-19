package io.sinq.provider

import io.sinq.SinqStream
import io.sinq.expression.Condition
import io.sinq.rs.{Column, Order, Table}

import scala.beans.BeanProperty
import scala.collection.mutable

case class QueryInfo(val stream: SinqStream) {
  lazy val selectFields = mutable.ArrayBuffer[Column]()
  lazy val fromTables = mutable.ArrayBuffer[Table]()
  lazy val groupByFields = mutable.ArrayBuffer[Column]()

  @BeanProperty
  var join: Join = _
  @BeanProperty
  var on: Condition = _
  @BeanProperty
  var whereCondition: Condition = _
  @BeanProperty
  var from: From = _
  @BeanProperty
  var result: Result = _
  @BeanProperty
  var having: Condition = _
  @BeanProperty
  var limit: (Int, Int) = _
  @BeanProperty
  var order: Order = _
}





