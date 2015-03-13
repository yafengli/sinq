package io.sinq.rs

import io.sinq.expression.ConditionII

import scala.beans.BeanProperty
import scala.collection.mutable

class QueryInfo {
  val select = mutable.ArrayBuffer[Column]()
  val from = mutable.ArrayBuffer[Table]()
  val params = mutable.ArrayBuffer[Any]()
  val groupBy = mutable.ArrayBuffer[Column]()

  @BeanProperty
  var join: Join = _

  @BeanProperty
  var where: String = _

  @BeanProperty
  var condition: ConditionII = _

  @BeanProperty
  var limit: (Int, Int) = _
  @BeanProperty
  var order: Order = _
}





