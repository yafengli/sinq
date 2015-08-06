package io.sinq.provider

trait From[T] {
  def from(tables: Table[_]*): Where[T]
}












