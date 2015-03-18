package test

import init.{STUDENT, TEACHER}
import io.sinq.SinqStream
import io.sinq.expression.{Eq, Ge, In, Le}
import io.sinq.rs.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class StringSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()

  test("SQL Build.") {
    val condition = Eq(STUDENT.id, 1).or(Le(STUDENT.id, 12).and(Ge(STUDENT.age, 11L).and(In(STUDENT.id, Seq(1, 2, 3))).or(Ge(STUDENT.age, 15L))))
    println("sql:" + condition.translate())
    println("params:" + condition.params())

    val query = sinq.select().from(STUDENT).join(TEACHER).on(Eq(STUDENT.teacher_id, TEACHER.id)).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)

    println("sql:" + query.sql())
    println("params:" + query.params())
  }
}
