package org.koala.sporm.rs

trait Column {
  def as(alias: String): String = if (alias == null) null else s" AS ${alias}"

  def toSql: String
}

object Column {
  def apply(columns: String*): Seq[Column] = {
    for (cl <- columns) yield new Column {
      override def toSql: String = s"${cl}"
    }
  }
}
