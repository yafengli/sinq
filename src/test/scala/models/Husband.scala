package models

import javax.persistence._
import org.sporm.jpa.JPAModel

/**
 * User: YaFengLi
 * Date: 12-12-3
 * Time: 下午1:28
 */
@Entity
@Table(name = "t_husband")
case class Husband(var name: String, var age: Int) {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_husband")
  @TableGenerator(name = "seq_t_husband", table = "seq_t_husband", allocationSize = 1)
  var id: Long = _

  @OneToOne(fetch = FetchType.EAGER, mappedBy = "husband")
  var teacher: Teacher = _

  def this() = this(null, -1)

  def this(name: String, age: Int, teacher: Teacher) = {
    this(name, age)
    this.teacher = teacher
  }
}

object Husband extends JPAModel[Husband]