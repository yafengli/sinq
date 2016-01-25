package test

import gen.{_STUDENT, _TEACHER}
import io.sinq.builder.ConditionBuilder
import io.sinq.expr.{Eq, Ge, In, Le}
import io.sinq.func.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.PGDBInit._

@RunWith(classOf[JUnitRunner])
class StringSuite extends FunSuite with BeforeAndAfter {

  test("SQL Build.") {
    val condition = Eq(_STUDENT.id, 1).or(Le(_STUDENT.id, 12).and(Ge(_STUDENT.age, 11L).and(In(_STUDENT.id, Seq(1, 2, 3))).or(Ge(_STUDENT.age, 15L))))
    val cb = ConditionBuilder()
    println("sql:" + cb.translate(condition))
    println("params:" + cb.params(condition))

    val query = sinq.from(_STUDENT).join(_TEACHER).on(Eq(_STUDENT.teacher, _TEACHER.id)).where(condition).orderBy(Order(ASC, _STUDENT.id)).limit(10, 0)

    println("sql:" + query.sql())
    println("params:" + query.params())
  }
}
