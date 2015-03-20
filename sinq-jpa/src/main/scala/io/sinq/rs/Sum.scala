package io.sinq.rs

import io.sinq.Column

case class Sum(val col: Column) extends Column {
  override def identifier(): String = s"sum(${col.identifier()}) as ${col.identifier}"
}
