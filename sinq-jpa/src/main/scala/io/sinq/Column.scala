package io.sinq

trait Column[+T] extends Alias {

  /**
   * @return 别名
   */
  override def as(): String = this.identifier().replace(".", "_")
}

object Column {

  def apply[T, K](t: Table[K], ct: Class[T], col: String): Column[T] = {
    new Column[T] {
      override def identifier(): String = s"${t.as}.${col}"
    }
  }
}
