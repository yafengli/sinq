package models.h2

import java.sql.Timestamp
import javax.persistence._

import scala.beans.BeanProperty

/**
  * Created by YaFengLi on 2016/1/25.
  */
@Entity
@Table(name = "t_person")
case class Person(@BeanProperty var name: String, @BeanProperty var age: Int) extends Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @OneToOne(cascade = Array(CascadeType.REMOVE), mappedBy = "person")
  @BeanProperty
  var zone: Zone = _

  @Column
  @BeanProperty
  var birthday: Timestamp = new Timestamp(System.currentTimeMillis())

  def this() = this(null, -1)

  def this(name: String, age: Int, zone: Zone) = {
    this(name, age)
    this.zone = zone
  }
}
