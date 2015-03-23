package models

import java.math.BigInteger
import javax.persistence._

import scala.beans.BeanProperty

/**
 * User: YaFengLi
 * Date: 12-12-3
 * Time: 下午1:28
 */
@Entity
@Table(name = "t_user")
case class User(@BeanProperty var name: String, @BeanProperty var age: Int) {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @OneToOne(cascade = Array(CascadeType.REMOVE), mappedBy = "user")
  @BeanProperty
  var address: Address = _

  def this() = this(null, -1)

  def this(name: String, age: Int, address: Address) = {
    this(name, age)
    this.address = address
  }
}