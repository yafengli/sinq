package io.sinq.builder

import io.sinq.rs.Table

case class From(info: QueryInfo) {
  def from(tables: Table*): Result = {
    info.from ++= tables
    Result(info)
  }
}
