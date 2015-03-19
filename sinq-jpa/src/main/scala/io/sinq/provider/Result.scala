package io.sinq.provider

import io.sinq.rs._

trait Result extends InfoProvider {

  def orderBy(order: Order): Result

  def limit(limit: Int, offset: Int): Result

  def sql(): String

  def params(): List[Any]

  def single(): Option[Any]

  def single[T](ct: Class[T]): Option[T]

  def collect(): List[Any]

  def collect[T](t: Class[T]): List[T]
}


