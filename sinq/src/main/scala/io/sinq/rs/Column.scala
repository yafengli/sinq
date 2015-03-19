package io.sinq.rs

trait Column extends Alias {
  /**
   * @return 别名
   */
  override def as(): String = this.identifier().replace(".", "_")
}

object Column {
  def apply(t: Table, columns: String*): Seq[Column] = {
    for (cl <- columns) yield new Column {
      override def identifier(): String = s"${t.as}.${cl}"
    }
  }

  def apply(columns: String*): Seq[Column] = {
    for (cl <- columns) yield new Column {
      override def identifier(): String = s"${cl}"
    }
  }

  def apply(col: String): Column = {
    new Column {
      override def identifier(): String = s"${col}"
    }
  }

  def apply(t: Table, col: String): Column = {
    new Column {
      override def identifier(): String = s"${t.as}.${col}"
    }
  }
}
