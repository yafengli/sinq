package io.sinq.expr

trait Tuple2Condition[T <: Any] extends Condition {
  def paramValue1: T

  def paramValue2: T

  override def values: List[Any] = List(paramValue1, paramValue2)
}
