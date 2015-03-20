package io.sinq.rs

import io.sinq.Column

case class Count(val col: Column) extends Column {
  override def identifier(): String = s"count(${col.identifier()}) as ${col.as()}"
}
