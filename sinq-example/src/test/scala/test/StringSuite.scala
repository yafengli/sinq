package test

import gen.{STUDENT, TEACHER}
import io.sinq.SinqStream
import io.sinq.builder.ConditionBuilder
import io.sinq.expression.{Eq, Ge, In, Le}
import io.sinq.func.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class StringSuite extends FunSuite with BeforeAndAfter {

  lazy val sinq = SinqStream("h2")

  test("SQL Build.") {
    val condition = Eq(STUDENT.id, 1).or(Le(STUDENT.id, 12).and(Ge(STUDENT.age, 11L).and(In(STUDENT.id, Seq(1, 2, 3))).or(Ge(STUDENT.age, 15L))))
    val cb = ConditionBuilder()
    println("sql:" + cb.translate(condition))
    println("params:" + cb.params(condition))

    val query = sinq.from(STUDENT).join(TEACHER).on(Eq(STUDENT.teacher, TEACHER.id)).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)

    println("sql:" + query.sql())
    println("params:" + query.params())
  }
}
