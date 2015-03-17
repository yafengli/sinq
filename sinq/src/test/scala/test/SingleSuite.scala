package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.{STUDENT, H2DB}
import io.sinq.SinqStream
import io.sinq.expression._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import H2DB._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class SingleSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()
  val condition = Eq(STUDENT.id, 1).or(Le(STUDENT.id, 12).and(Ge(STUDENT.age, 11L).and(In(STUDENT.id, Seq(1, 2, 3))).or(Ge(STUDENT.age, 15L))))

  before {
    open
  }

  after {
    close
  }
  test("Single.") {
    val latch = new CountDownLatch(1)
    Future {
      val query = sinq.select(STUDENT.* : _*).from(STUDENT).where(condition)
      query.single() match {
        case Some(Array(id, name, age)) => println(s"id:${id} name:${name} age:${age}")
        case None => println("None")
      }
      latch.countDown()
    } onComplete {
      case Success(r) => println(s"Success-1:${r}")
      case Failure(t) => println(s"Failure-1:${t}")
    }
    latch.await(10, TimeUnit.SECONDS)
  }
}

