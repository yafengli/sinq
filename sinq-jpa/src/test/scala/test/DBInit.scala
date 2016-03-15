package test

import java.net.InetAddress
import java.util.concurrent.{CountDownLatch, TimeUnit}

import io.sinq.SinqStream
import io.sinq.func.Count
import io.sinq.util.JPA
import jpa.impl.ActiveJPA
import models.h2.init.T_PERSON
import models.h2.{Person, Zone}
import models.postgres.init.T_USER
import models.postgres.{Address, User}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DBInit {
  lazy val logger = LoggerFactory.getLogger(DBInit.getClass)
  val h2 = "h2"
  val postgres = "postgres"

  lazy val sinq_h2 = SinqStream(h2)
  lazy val sinq_pg = SinqStream(postgres)

  implicit lazy val ua = ActiveJPA[jpa.entity.User](h2)

  val count = 4
  val latch = new CountDownLatch(count)

  def init(): Unit = {
    if (latch.getCount == count) {
      println(s"##########DB Server start.###############")
      println(s"sinq_h2:${sinq_h2}")
      println(s"sinq_pg:${sinq_pg}")
      //data models.postgres.init
      h2Init(sinq_h2)
      pgInit(sinq_pg)
      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.releaseAll()
        println(s"##########DB Server closed.###############")
      }
    }
  }

  private def h2Init(sinq: SinqStream): Unit = {
    val count = sinq.select(Count[Long](T_PERSON.id)).from(T_PERSON).single().getOrElse(0L)
    if (count.longValue() <= 0) {
      (0 to 2).foreach {
        i =>
          val person = Person(s"user-${i}", i)
          sinq.insert(person)
          logger.info(s"person:${person.id} ${person.name} ${person.age}")
          val zone = new Zone()
          zone.setName("192.168.0.234")
          zone.setNum(999)
          zone.setPerson(person)
          sinq.insert(zone)
      }
    }
  }
  private def pgInit(sinq: SinqStream): Unit = {
    val count = sinq.select(Count[Long](T_USER.id)).from(T_USER).single().getOrElse(0L)
    if (count.longValue() <= 0) {
      (0 to 2).foreach {
        i =>
          val user = User(s"user-${i}", i)
          sinq.insert(user)
          logger.info(s"user:${user.id} ${user.name} ${user.age}")
          val address = new Address()
          address.setIpAddr(InetAddress.getByName("192.168.0.234"))
          address.setUser(user)
          sinq.insert(address)
      }
    }
  }
}
