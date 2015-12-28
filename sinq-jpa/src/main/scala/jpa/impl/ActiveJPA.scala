package jpa.impl

import jpa.active.Record

case class ActiveJPA[T: Manifest](val pn: String) extends Record[T] {
  def getType = implicitly[Manifest[T]].runtimeClass
}

