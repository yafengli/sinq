package io.sinq.rs

trait OrderDesc extends Alias {
  /**
   * @return 别名
   */
  override def as(): String = identifier()
}

object ASC extends OrderDesc {
  override def identifier(): String = "ASC"
}

object DESC extends OrderDesc {
  override def identifier(): String = "DESC"
}

case class Order(val orderDesc: OrderDesc, val cols: Column*)
