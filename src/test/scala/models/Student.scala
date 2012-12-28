package models

import javax.persistence._
import java.util.Set
import java.util.HashSet
import org.koala.sporm.jpa.CQModel


@Entity
@Table(name = "t_student")
class Student(var name: String, var age: Int, var address: String) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_student")
  @TableGenerator(name = "seq_t_student", table = "seq_t_student", allocationSize = 1)
  var id: Long = _

  @ManyToOne()
  @JoinColumn(name = "teacher_id")
  var teacher: Teacher = _


  @OneToMany(cascade = Array(CascadeType.REMOVE), mappedBy = "student")
  var books: Set[Book] = new HashSet[Book]()

  def this() = this(null, -1, null)

  def this(name: String) = this(name, -1, null)

  def this(name: String, age: Int) = this(name, age, null)

  def this(name: String, age: Int, address: String, teacher: Teacher) = {
    this(name, age, address)
    this.teacher = teacher
  }

  override def toString = id + ":" + name + ":" + age + ":" + address

}

object Student extends CQModel[Student]