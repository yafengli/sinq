package org.koala.sporm.rs

import org.koala.sporm.WhereII

import scala.beans.BeanProperty
import scala.collection.mutable

class SelectInfo {
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

case class Table(val name: String, val as: String) extends Alias

case class Order(val order: String, val cols: Column*)

trait Join {
  def on(condition: ConditionII): WhereII
}

trait JoinInner extends Join

trait JoinLeft extends Join

trait JoinRight extends Join

trait JoinFull extends Join


