package org.koala.sporm.jpa

import support.TemplateCriteriaQuery

/**
 * User: YaFengLi
 * Date: 13-1-22
 * Time: 上午10:33
 */
abstract class Model[T: Manifest] extends TemplateCriteriaQuery[T] {
  //2.10+
  def getType = implicitly[Manifest[T]].runtimeClass

  //@Deprecated  def getType = implicitly[Manifest[T]].erasure //2.9.2
}
