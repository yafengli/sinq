package models

import javax.persistence._
import scala.beans.BeanProperty
import java.util

@Entity
@Table(name = "t_user")
case class User(@BeanProperty var name: String, @BeanProperty var age: Int) extends Serializable{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @OneToOne(cascade = Array(CascadeType.REMOVE), mappedBy = "user")
  @BeanProperty
  var address: Address = _


  @ManyToMany(cascade = Array(CascadeType.REMOVE), fetch = FetchType.EAGER, mappedBy = "users")
  var families: util.Set[Family] = new util.HashSet[Family]()

  def this() = this(null, -1)

  def this(name: String, age: Int, address: Address) = {
    this(name, age)
    this.address = address
  }
}
