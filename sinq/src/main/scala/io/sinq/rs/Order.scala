package io.sinq.rs

trait OrderDesc extends Alias

object ASC extends OrderDesc {
  override def name(): String = "ASC"
}

object DESC extends OrderDesc {
  override def name(): String = "DESC"
}

case class Order(val orderDesc: OrderDesc, val cols: Column*)
