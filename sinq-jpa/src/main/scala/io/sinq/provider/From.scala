package io.sinq.provider

import io.sinq.Table
import io.sinq.rs._

trait From {
  def from(tables: Table*): Where
}












