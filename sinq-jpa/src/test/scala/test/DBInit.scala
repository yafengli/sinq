package test

import java.math.BigInteger
import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.T_USER
import io.sinq.SinqStream
import io.sinq.func.Count
import io.sinq.util.JPA
import jpa.impl.ActiveJPA
import models.User
import models.ext.InetObject
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DBInit {
  lazy val logger = LoggerFactory.getLogger(DBInit.getClass)
  //val pn = "h2"
  val pn = "postgres"

  implicit lazy val sinq = SinqStream(pn)
  implicit lazy val ua = ActiveJPA[jpa.entity.User](pn)

  // Test Method Number
  val count = 1
  val latch = new CountDownLatch(count)

  def init(): Unit = {
    if (latch.getCount == count) {
      println(s"##########DB Server start.###############")
      JPA.initPersistenceName(pn)
      //data init
      dataStore
      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.release()
        println(s"##########DB Server closed.###############")
      }
    }
  }

  private def dataStore(implicit sinq: SinqStream): Unit = {
    val count = sinq.select(Count(T_USER.id)).from(T_USER).single().getOrElse(0L)
    if (count <= 0) {
      (0 to 2).foreach {
        i =>
          val user = User(s"user-${i}", i)
          sinq.insert(user)
          logger.info(s"user:${user.id} ${user.name} ${user.age}")
          val address = new models.Address()
//          address.setIpAddr(InetObject("192.168.0.234"))
          address.setUser(user)
          sinq.insert(address)
      }
    }
  }
}
