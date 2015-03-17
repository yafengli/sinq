package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.{STUDENT, H2DB}
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import H2DB._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()
  val condition = Between(STUDENT.id, -1, 12).and(Ge(STUDENT.age, 5L).or(In(STUDENT.name, Seq("YaFengLi:1", "YaFengLi:3"))))

  before {
    open
  }

  after {
    close
  }

  test("Collect.") {
    val latch = new CountDownLatch(1)

    Future {
      latch.countDown()
      val query = sinq.select(STUDENT.* : _*).from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
      query.collect(classOf[Array[Any]]).foreach {
        case Array(id, name) => println(s"#id:${id} name:${name}")
        case _ =>
      }
    } onComplete {
      case Success(s) => println(s)
      case Failure(t) => println(t)
    }
    latch.await(20, TimeUnit.SECONDS)
  }
}


