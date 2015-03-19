package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.Count
import io.sinq.util.JPA
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class GroupBySuite extends FunSuite with BeforeAndAfter {
  val condition = Between(STUDENT.id, 1, 12).or(Ge(STUDENT.age, 15L))
  before {
    JPA.initPersistenceName("postgres")
  }
  after {
    JPA.release()
  }

  test("Group By.") {
    val sinq = SinqStream("postgres")
    val latch = new CountDownLatch(12)
    Future {
      (0 to 5).foreach {
        i =>
          val query = sinq.select(Count(STUDENT.id), Count(STUDENT.name)).from(STUDENT).where(condition).groupBy(STUDENT.id)
          query.single() match {
            case Some(Array(id, name)) => println(s"id:${id} name:${name}")
            case None => println("None")
          }
          latch.countDown()
      }
    } onComplete {
      case Success(r) =>
      case Failure(t) => println(s"Failure-1:${t}")
    }

    Future {
      (0 to 5).foreach {
        i =>
          val query = sinq.select(Count(STUDENT.id)).from(STUDENT).where(condition).groupBy(STUDENT.id)
          query.single() match {
            case Some(count) => println(s"count:${count}")
            case None => println("None")
          }
          latch.countDown()
      }
    } onComplete {
      case Success(r) =>
      case Failure(t) => println(s"Failure-2:${t}")
    }
    latch.await(10, TimeUnit.SECONDS)
  }
}


