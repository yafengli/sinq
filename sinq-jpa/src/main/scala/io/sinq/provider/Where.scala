package io.sinq.provider

import io.sinq.Table
import io.sinq.expression.Condition
import io.sinq.rs._

trait Where[T] extends Result[T] {

  def where(condition: Condition = null): GroupBy[T]

  def join[K](table: Table[K]): JoinInner[T,K]

  def joinLeft[K](table: Table[K]): JoinLeft[T,K]

  def joinRight[K](table: Table[K]): JoinRight[T,K]

  def joinFull[K](table: Table[K]): JoinFull[T,K]
}


