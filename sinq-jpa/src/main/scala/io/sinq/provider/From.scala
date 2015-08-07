package io.sinq.provider

trait From[T] {
  /**
   * @param tables 表(Table)序列。
   * @return 条件语句(Where)。
   */
  def from(tables: Table[_]*): Where[T]
}












