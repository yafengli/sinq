package io.sinq.func

import io.sinq.Column
import io.sinq.provider.{Column, Alias}

trait OrderDesc extends Alias

object ASC extends OrderDesc {
  override def identifier(): String = "ASC"
}

object DESC extends OrderDesc {
  override def identifier(): String = "DESC"
}

case class Order(val orderDesc: OrderDesc, val cols: Column[_]*)
