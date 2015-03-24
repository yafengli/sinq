package io.sinq.provider

import io.sinq.Table
import io.sinq.func._

trait From[T] {
  def from(tables: Table[_]*): Where[T]
}












