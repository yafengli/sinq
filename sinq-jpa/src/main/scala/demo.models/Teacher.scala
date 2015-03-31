package demo.models

import javax.persistence._

import scala.beans.BeanProperty

@Entity
@Table(name = "t_teacher")
case class Teacher(@BeanProperty var name: String, @BeanProperty var age: Int) {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "t_user_teacher", joinColumns = Array(
    new JoinColumn(name = "t_id")
  ), inverseJoinColumns = Array(
    new JoinColumn(name = "u_id")
  ))
  var users: Set[User] = _
}
