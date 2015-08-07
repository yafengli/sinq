package io.sinq.provider

import java.sql.Connection

trait Provider {
  /**
   * @param call 参数为数据库连接(Connection)返回为T类型的函数。
   * @tparam T 返回Option数据的类型。
   * @return Option[T]对象。
   */
  def withConnection[T](call: Connection => T): Option[T]
}
