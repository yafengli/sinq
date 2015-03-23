package io.sinq.provider

trait InfoProvider[T] {
  def info: QueryInfo
}
