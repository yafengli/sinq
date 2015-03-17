package io.sinq.provider

import io.sinq.rs.Table

case class From(info: QueryInfo) {
  def from(tables: Table*): Result = {
    info.from ++= tables
    ResultImpl(info)
  }
}
