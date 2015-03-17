package io.sinq.provider

import io.sinq.rs._

case class From(info: QueryInfo) {
  def from(tables: Table*): Result = {
    info.from ++= tables
    ResultImpl(info)
  }

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
