package org.koala.sporm.expression


class In extends Condition

object In {
  def apply(column: String, cols: Set[Any]): Condition = {
    val in = new In
    val key = in.key(column)
    in.paramsMap += (key -> cols)
    in.linkCache.append(s"${column} in (:${key})")
    in
  }
}
