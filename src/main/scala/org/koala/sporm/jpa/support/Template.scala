package org.koala.sporm.jpa.support

trait Template[T] {

  def getType: Class[_]

  implicit def generateModel(entity: T) = BaseOperator[T](entity)
}
