package models.postgres

import java.net.InetAddress
import java.sql.Timestamp
import javax.persistence._

import models.postgres.ext.InetJpaConverter

import scala.beans.BeanProperty

@Entity
@Table(name = "t_address")
class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = _

  @Column
  @BeanProperty
  var createdate: Timestamp = new Timestamp(System.currentTimeMillis())

  @Column
  @BeanProperty var num: Int = _

  @Column(columnDefinition = "inet")
  @Convert(converter = classOf[InetJpaConverter])
  @BeanProperty
  var ipAddr: InetAddress = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "u_id")
  @BeanProperty
  var user: User = _
}
