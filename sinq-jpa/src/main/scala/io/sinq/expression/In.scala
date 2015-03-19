package io.sinq.expression

import io.sinq.rs.Column

case class In[T](val column: Column, val paramValue: Seq[T]) extends Tuple1Condition[Seq[T]] {
  override def link: String = "in"
}
