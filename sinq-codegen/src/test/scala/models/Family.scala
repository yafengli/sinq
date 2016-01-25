package models

import java.util
import javax.persistence._

import models.postgres

import scala.beans.BeanProperty

@Entity
@Table(name = "t_family")
case class Family(@BeanProperty var name: String, @BeanProperty var age: Int) {
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
  var users: util.Set[postgres.User] = new util.HashSet[postgres.User]()
}
