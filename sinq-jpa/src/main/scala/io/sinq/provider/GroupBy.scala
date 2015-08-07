package io.sinq.provider

trait GroupBy[T] extends Result[T] {
  /**
   * @param cols 字段(Column)序列。
   * @return GroupBy条件Having。
   */
  def groupBy(cols: Column[_]*): Having[T]
}












