package models

import javax.persistence._
import java.util
import org.koala.sporm.jpa.CQModel

import scala.beans.BeanProperty

@Entity
@Table(name = "t_teacher")
case class Teacher(@BeanProperty var name: String, @BeanProperty var age: Int, @BeanProperty var address: String) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_teacher")
  @TableGenerator(name = "seq_t_teacher", table = "seq_t_teacher", allocationSize = 1)
  var id: Long = _

  @OneToMany(cascade = Array(CascadeType.REMOVE), mappedBy = "teacher")
  var students: util.Set[Student] = new util.HashSet[Student]()

  @OneToOne(cascade = Array(CascadeType.REMOVE), mappedBy = "teacher")
  var husband: Husband = _

  def this() = this(null, -1, null)

}

object Teacher extends CQModel[Teacher]
