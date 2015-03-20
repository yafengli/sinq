package io.sinq

trait Alias {
  /**
   * @return 标识
   */
  def identifier(): String

  /**
   * @return 别名
   */
  def as(): String
}
