package io.sinq.provider

import io.sinq.rs._

trait Result[T] extends InfoProvider[T] {

  def orderBy(order: Order): Result[T]

  def limit(limit: Int, offset: Int): Result[T]

  def sql(): String

  def params(): List[_]

  def single(): Option[T]

  def collect(): List[T]
}


