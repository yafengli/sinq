package io.sinq.provider

import io.sinq.rs.Column

trait GroupBy extends Result {
  def groupBy(cols: Column*): Having
}












