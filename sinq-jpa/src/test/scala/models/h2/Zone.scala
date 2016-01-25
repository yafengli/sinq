package models.h2

import java.sql.Timestamp
import javax.persistence._

import scala.beans.BeanProperty

@Entity
@Table(name = "t_zone")
class Zone {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = _

  @Column
  @BeanProperty
  var createdate: Timestamp = new Timestamp(System.currentTimeMillis())

  @Column
  @BeanProperty var num: Int = _

  @Column()
  @BeanProperty
  var name: String = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "p_id")
  @BeanProperty
  var person: Person = _
}
