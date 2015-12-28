package jpa.entity

import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name = "demo_test_user")
class User {
  @Id @GeneratedValue(strategy = GenerationType.AUTO) @BeanProperty var id: Long = _

  @BeanProperty var name: String = _
  @BeanProperty var age: Int = _
}
