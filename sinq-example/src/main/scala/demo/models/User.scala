package demo.models

import javax.persistence._

import org.koala.sporm.jpa.CQModel

import scala.beans.BeanProperty

@Entity
@Table(name = "t_user")
case class User(@BeanProperty var name: String, @BeanProperty var price: Int) {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_user")
  @TableGenerator(name = "seq_t_user", table = "seq_t_user", allocationSize = 1)
  @BeanProperty
  var id: Long = _

  @Column
  @BeanProperty
  val age: Int = -1

  @Column
  @BeanProperty
  val address: String = null
}