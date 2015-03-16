package models

import java.util
import javax.persistence._

import scala.beans.BeanProperty

@Entity
@Table(name = "t_teacher")
case class Teacher(@BeanProperty var name: String, @BeanProperty var age: Int = -1, @BeanProperty var address: String) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_teacher")
  @TableGenerator(name = "seq_t_teacher", table = "seq_t_teacher", allocationSize = 1)
  @BeanProperty
  var id: Long = _

  @OneToMany(cascade = Array(CascadeType.REMOVE), mappedBy = "teacher")
  @BeanProperty
  var students: util.Set[Student] = new util.HashSet[Student]()

  @OneToOne(cascade = Array(CascadeType.REMOVE), mappedBy = "teacher")
  @BeanProperty
  var husband: Husband = _

  def this() = this(null, -1, null)

}
