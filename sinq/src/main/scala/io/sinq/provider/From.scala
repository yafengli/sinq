package io.sinq.provider

import io.sinq.rs._

trait From extends Where {

  def join(table: Table): JoinInner

  def joinLeft(table: Table): JoinLeft

  def joinRight(table: Table): JoinRight

  def joinFull(table: Table): JoinFull
}

case class FromImpl(val info: QueryInfo) extends From {

  def join(table: Table): JoinInner = {
    JoinInner(table, info)
  }

  def joinLeft(table: Table): JoinLeft = {
    JoinLeft(table, info)
  }

  def joinRight(table: Table): JoinRight = {
    JoinRight(table, info)
  }

  def joinFull(table: Table): JoinFull = {
    JoinFull(table, info)
  }
}
