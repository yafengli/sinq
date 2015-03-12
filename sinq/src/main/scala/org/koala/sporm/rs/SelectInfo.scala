package org.koala.sporm.rs

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
  var limit: (Int, Int) = _
}

case class Table(val name: String, val as: String)

trait Join

trait JoinInner extends Join

trait JoinLeft extends Join

trait JoinRight extends Join

trait JoinFull extends Join


