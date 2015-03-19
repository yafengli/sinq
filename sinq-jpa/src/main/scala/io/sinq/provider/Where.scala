package io.sinq.provider

import io.sinq.expression.Condition
import io.sinq.rs._

trait Where extends Result {

  def where(condition: Condition = null): GroupBy

  def join(table: Table): JoinInner

  def joinLeft(table: Table): JoinLeft

  def joinRight(table: Table): JoinRight

  def joinFull(table: Table): JoinFull
}


