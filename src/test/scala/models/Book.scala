package models

import javax.persistence._
import org.koala.sporm.jpa.CQModel

import scala.beans.BeanProperty

/**
 * User: YaFengLi
 * Date: 12-12-3
 * Time: 上午11:06
 */
@Entity
@Table(name = "t_book")
case class Book(@BeanProperty var name: String, @BeanProperty var price: Int) {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_book")
  @TableGenerator(name = "seq_t_book", table = "seq_t_book", allocationSize = 1)
  var id: Long = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "student_id")
  var student: Student = _

  def this() = this(null, -1)

  def this(name: String, price: Int, student: Student) = {
    this(name, price)
    this.student = student
  }

}

object Book extends CQModel[Book]
