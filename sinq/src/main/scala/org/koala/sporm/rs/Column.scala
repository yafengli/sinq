package org.koala.sporm.rs

trait Alias {
  def name(): String
}

trait Column extends Alias

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

  def apply(col: String): Column = {
    new Column {
      override def name(): String = s"${col}"
    }
  }
}
