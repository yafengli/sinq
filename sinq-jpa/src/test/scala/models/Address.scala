package models

import java.sql.Timestamp
import javax.persistence._

import models.ext.{InetConverter, InetObject}
import org.eclipse.persistence.annotations

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

  @Column(columnDefinition = "inet")
  @annotations.Converter(name = "inetConverter", converterClass = classOf[InetConverter])
  @annotations.Convert("inetConverter")
  //  @Convert(converter = classOf[InetConverter])
  @BeanProperty
  var ipAddr: InetObject = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "u_id")
  @BeanProperty
  var user: User = _
}
