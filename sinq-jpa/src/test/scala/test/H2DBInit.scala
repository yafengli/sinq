package test

import java.math.BigInteger
import java.util.concurrent.{CountDownLatch, TimeUnit}

import io.sinq.SinqStream
import io.sinq.func.Count
import io.sinq.util.JPA
import models.h2.init.T_PERSON
import models.h2.{Person, Zone}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object H2DBInit {
  lazy val logger = LoggerFactory.getLogger(PGDBInit.getClass)
  val pn = "h2"

  implicit lazy val sinq = SinqStream(pn)

  // Test Method Number
  val count = 1
  val latch = new CountDownLatch(count)

  def init(): Unit = {
    if (latch.getCount == count) {
      println(s"##########DB Server start.###############")
      JPA.initPersistenceName(pn)
      dataStore
      Future {
        latch.await(20, TimeUnit.SECONDS)
        JPA.release()
        println(s"##########DB Server closed.###############")
      }
    }
  }

  private def dataStore(implicit sinq: SinqStream): Unit = {
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
}
