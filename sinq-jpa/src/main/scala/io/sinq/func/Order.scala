package io.sinq.func

import io.sinq.provider.{Alias, Column}

trait Order extends Alias {
  def cols: Seq[Column[_]]
}

case class ASC(val cols: Column[_]*) extends Order {
  override def identifier(): String = "ASC"
}

case class DESC(val cols: Column[_]*) extends Order {
  override def identifier(): String = "DESC"
}
