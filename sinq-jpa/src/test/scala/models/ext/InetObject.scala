package models.ext

import java.net.InetAddress

import scala.beans.BeanProperty

/**
  * Created by YaFengLi on 2016/1/21.
  */
case class InetObject(@BeanProperty name: String) {
  def inet: InetAddress = InetAddress.getByName(name)

  override def toString: String = this.name
}
