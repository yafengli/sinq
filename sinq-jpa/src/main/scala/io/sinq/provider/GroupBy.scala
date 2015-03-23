package io.sinq.provider

import io.sinq.Column

trait GroupBy[T] extends Result[T] {
  def groupBy(cols: Column[_]*): Having[T]
}












