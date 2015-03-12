package org.koala.sporm.rs

trait Column {
  def name(): String
}

object Column {
  def apply(t: Table, columns: String*): Seq[Column] = {
    for (cl <- columns) yield new Column {
      override def name(): String = s"${t.as}.${cl}"
    }
  }

  def apply(columns: String*): Seq[Column] = {
    for (cl <- columns) yield new Column {
      override def name(): String = s"${cl}"
    }
  }
}
