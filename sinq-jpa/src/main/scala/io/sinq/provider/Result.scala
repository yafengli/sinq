package io.sinq.provider

import io.sinq.func._

trait Result[T] extends Aggregation {

  def orderBy(order: Order): Result[T]

  def limit(limit: Int, offset: Int): Result[T]

  def sql(): String

  def params(): List[_]

  def single(): Option[T]

  def collect(): List[T]
}


