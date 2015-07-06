package test

import gen.{STUDENT, TEACHER}
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.func.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class JoinSuite extends FunSuite with BeforeAndAfter {
  val condition = Between(STUDENT.id, 1, 12).or(In(STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3")))
  lazy val sinq = SinqStream("h2")
  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }
  test("Join.") {
    val query = sinq.select(STUDENT.id, STUDENT.name, TEACHER.id, TEACHER.name).from(STUDENT).join(TEACHER).on(Eq(STUDENT.teacher, TEACHER.id)).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
    println("sql:" + query.sql())
    query.collect().foreach(t => println(s"T:${t}"))
  }
}

