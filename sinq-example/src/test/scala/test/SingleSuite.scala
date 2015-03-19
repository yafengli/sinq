package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.util.JPA
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class SingleSuite extends FunSuite with BeforeAndAfter {

  before {
    JPA.initPersistenceName("postgres")
  }
  after {
    JPA.release()
  }

  test("Single.") {
    val sinq = SinqStream("postgres")
    val latch = new CountDownLatch(1)
    Future {
      val query = sinq.select(STUDENT.* : _*).from(STUDENT).where(Eq(STUDENT.id, 1))
      query.single() match {
        case Some(Array(id, name, age)) => println(s"#id:${id} name:${name} age:${age}")
        case None => println("None")
      }
      sinq.find(1L, classOf[Student]) match {
        case Some(s) => println(s">id:${s.id} name:${s.name} age:${s.age}")
        case None =>
      }

      latch.countDown()
    } onComplete {
      case Success(r) =>
      case Failure(t) => println(s"Failure-1:${t}")
    }
    latch.await(10, TimeUnit.SECONDS)
  }
}

