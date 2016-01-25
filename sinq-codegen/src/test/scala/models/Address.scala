package models

import java.util.Date
import javax.persistence._

import scala.beans.BeanProperty

/**
  * User: YaFengLi
  * Date: 12-12-3
  * Time: 下午1:28
  */
@Entity
@Table(name = "t_address")
case class Address(@BeanProperty var name: String, @BeanProperty var num: Int) {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @OneToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "u_id")
  @BeanProperty
  var user: User = _

  @BeanProperty
  var createDate = new Date()

  def this() = this(null, -1)

  def this(name: String, age: Int, user: User) = {
    this(name, age)
    this.user = user
  }
}