package io.sinq.provider

import java.sql.Connection

trait Provider {
  def withConnection[T](call: Connection => T): Option[T]
}
