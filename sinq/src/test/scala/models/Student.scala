package models

import javax.persistence._

import scala.beans.BeanProperty


@Entity
@Table(name = "t_student")
case class Student(@BeanProperty var name: String, @BeanProperty var age: Int, @BeanProperty var address: String) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_student")
  @TableGenerator(name = "seq_t_student", table = "seq_t_student", allocationSize = 1)
  @BeanProperty
  var id: Long = _

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "teacher_id")
  @BeanProperty
  var teacher: Teacher = _

  def this() = this(null, -1, null)

  def this(name: String) = this(name, -1, null)

  def this(name: String, age: Int) = this(name, age, null)

  def this(name: String, age: Int, address: String, teacher: Teacher) = {
    this(name, age, address)
    this.teacher = teacher
  }

  override def toString = id + ":" + name + ":" + age + ":" + address
}