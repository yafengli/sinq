package io.sinq.provider

import io.sinq.func._
import io.sinq.provider.Table

trait From[T] {
  def from(tables: Table[_]*): Where[T]
}












