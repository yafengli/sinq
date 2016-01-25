package models

import javax.persistence._

import scala.beans.BeanProperty

/**
 * User: YaFengLi
 * Date: 12-12-3
 * Time: 下午1:28
 */
@Entity
@Table(name = "e_address")
case class Address(@BeanProperty var name: String, @BeanProperty var num: Int) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_e_address")
  @TableGenerator(name = "seq_e_address", table = "seq_e_address", allocationSize = 1)
  @BeanProperty
  var id: Long = _

  @OneToOne(fetch = FetchType.EAGER, optional = false) @JoinColumn(name = "u_id") @BeanProperty
  var user: User = _

  def this() = this(null, -1)

  def this(name: String, age: Int, user: User) = {
    this(name, age)
    this.user = user
  }
}
