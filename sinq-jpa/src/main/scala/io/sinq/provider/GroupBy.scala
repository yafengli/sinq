package io.sinq.provider

trait GroupBy[T] extends Result[T] {
  def groupBy(cols: Column[_]*): Having[T]
}












