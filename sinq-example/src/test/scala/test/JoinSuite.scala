package test

import init.{STUDENT, TEACHER}
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.func.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class JoinSuite extends FunSuite with BeforeAndAfter {
  val condition = Between(STUDENT.id, 1, 12).or(In(STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3")))
  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }
  test("Join.") {
    val sinq = SinqStream("h2")
    val query = sinq.from(STUDENT).join(TEACHER).on(Eq(STUDENT.teacher_id, TEACHER.id)).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
    query.collect().foreach(t => println(s"T:${t}"))
  }
}

