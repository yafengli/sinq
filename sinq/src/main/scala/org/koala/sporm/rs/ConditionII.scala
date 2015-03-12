package org.koala.sporm.rs

import scala.collection.mutable

trait ConditionII {

  def bind(): mutable.ArrayBuffer[Any]

  def build(): String
}

