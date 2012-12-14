package models

import javax.persistence._
import metamodel.{StaticMetamodel, SingularAttribute}
import org.koala.sporm.jpa.JPQLModel

/**
 * User: YaFengLi
 * Date: 12-12-3
 * Time: 上午11:06
 */
@Entity
@Table(name = "t_book")
case class Book(var name: String, var price: Int) {

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

object Book extends JPQLModel[Book]

@StaticMetamodel(classOf[Book])
object Book_ {
  val id: SingularAttribute[Book, Int] = _
  val name: SingularAttribute[Book, String] = _
  val price: SingularAttribute[Book, Int] = _
}

