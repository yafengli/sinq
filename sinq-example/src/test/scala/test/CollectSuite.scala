package test

import java.util.concurrent.{CountDownLatch, TimeUnit}

import init.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.{ASC, Order}
import io.sinq.util.JPA
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {
  val condition = Between(STUDENT.id, -1, 12).and(Ge(STUDENT.age, 5L).or(In(STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3"))))

  before {
    JPA.initPersistenceName("postgres")
  }
  after {
    JPA.release()
  }
  test("Collect.") {
    val sinq = SinqStream("postgres")
    val latch = new CountDownLatch(2)

    Future {
      val query = sinq.select().from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
      query.collect(classOf[Student]).foreach(t => println(s"#id:${t.id} name:${t.name} age:${t.age}"))
      latch.countDown()
    } onComplete {
      case Success(s) =>
      case Failure(t) => println(t)
    }

    Future {
      val query = sinq.select(STUDENT.* : _*).from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
      query.collect().foreach {
        case Array(id, name, age) => println(s">id:${id} name:${name} age:${age}")
      }

      println("#########################")
      sinq.select(STUDENT.id, STUDENT.name).from(STUDENT).where(Ge(STUDENT.id, 1)).collect().foreach { case Array(id, name) => println(s"id:${id} name:${name}") }
      println("#########################")
      sinq.select(STUDENT.id).from(STUDENT).where(Ge(STUDENT.id, 1)).collect().foreach { id => println(s"id:${id}") }

      latch.countDown()
    } onComplete {
      case Success(s) =>
      case Failure(t) => println(t)
    }
    latch.await(20, TimeUnit.SECONDS)
  }
}


