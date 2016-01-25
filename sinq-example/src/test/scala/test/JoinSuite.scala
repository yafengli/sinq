package test

import gen.{_STUDENT, _TEACHER}
import io.sinq.expr._
import io.sinq.func.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.PGDBInit._

@RunWith(classOf[JUnitRunner])
class JoinSuite extends FunSuite with BeforeAndAfter {
  val condition = Between(_STUDENT.id, 1, 12).or(In(_STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3")))
  before {
    init()
  }
  after {
    latch.countDown()
  }
  test("Join.") {
    val query = sinq.select(_STUDENT.id, _STUDENT.name, _TEACHER.id, _TEACHER.name).from(_STUDENT).join(_TEACHER).on(Eq(_STUDENT.teacher, _TEACHER.id)).where(condition).orderBy(Order(ASC, _STUDENT.id)).limit(10, 0)
    println("sql:" + query.sql())
    query.collect().foreach(t => println(s"T:${t}"))
  }
}

