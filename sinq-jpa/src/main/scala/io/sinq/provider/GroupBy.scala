package io.sinq.provider

import io.sinq.Column

trait GroupBy extends Result {
  def groupBy(cols: Column*): Having
}












