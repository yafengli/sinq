package io.sinq.provider

import io.sinq.rs._

trait From extends Result {
  def from(tables: Table*): Where
}












