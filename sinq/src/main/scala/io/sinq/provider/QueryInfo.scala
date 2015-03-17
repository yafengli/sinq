package io.sinq.provider

import io.sinq.SinqStream
import io.sinq.expression.Condition
import io.sinq.rs.{Column, Join, Order, Table}

import scala.beans.BeanProperty
import scala.collection.mutable

case class QueryInfo(val stream: SinqStream) {
  lazy val select = mutable.ArrayBuffer[Column]()
  lazy val from = mutable.ArrayBuffer[Table]()
  lazy val groupBy = mutable.ArrayBuffer[Column]()

  @BeanProperty
  var join: Join = _

  @BeanProperty
  var where: String = _

  @BeanProperty
  var condition: Condition = _

  @BeanProperty
  var limit: (Int, Int) = _
  @BeanProperty
  var order: Order = _
}





