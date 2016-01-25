package models

import javax.persistence._

import scala.beans.BeanProperty

/**
  * User: YaFengLi
  * Date: 12-12-3
  * Time: 下午1:28
  */
@Entity
@Table(name = "e_user")
case class User(@BeanProperty var name: String, @BeanProperty var age: Int) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_e_user")
  @TableGenerator(name = "seq_e_user", table = "seq_e_user", allocationSize = 1)
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
