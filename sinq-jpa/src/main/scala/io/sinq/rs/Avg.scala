package io.sinq.rs

import io.sinq.Column

case class Avg(val col: Column) extends Column {

  override def identifier(): String = s"avg(${col.identifier()}) as ${col.identifier()}"
}
