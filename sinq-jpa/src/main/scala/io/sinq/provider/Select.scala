package io.sinq.provider

import io.sinq.rs._

trait Select {
  def from(table: Table*): From
}

case class SelectImpl(val info: QueryInfo) extends Select {
  def from(tables: Table*): From = {
    info.from ++= tables
    FromImpl(info)
  }
}












