package models.postgres

import java.util
import javax.persistence._

import scala.beans.BeanProperty

/**
  * Created by YaFengLi on 2016/1/25.
  */
@Entity
@Table(name = "t_family")
case class Family(@BeanProperty var name: String, @BeanProperty var age: Int) {
  def this() = this(null, -1)

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "t_user_family", joinColumns = Array(
    new JoinColumn(name = "f_id")
  ), inverseJoinColumns = Array(
    new JoinColumn(name = "u_id")
  ))
  var users: util.Set[User] = new util.HashSet[User]()
}
