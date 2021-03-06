package io.sinq.expr

import io.sinq.provider.Column

case class In[T, K](override val column: Column[K], override val paramValue: Seq[T]) extends Tuple1Condition[Seq[T]] {
  override def link: String = "in"
}
