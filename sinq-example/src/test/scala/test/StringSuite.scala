package test

import gen.{T_STUDENT, T_TEACHER}
import io.sinq.builder.ConditionBuilder
import io.sinq.expr.{Eq, Ge, In, Le}
import io.sinq.func.ASC
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class StringSuite extends FunSuite with BeforeAndAfter {

  test("SQL Build.") {
    val condition = Eq(T_STUDENT.id, 1).or(Le(T_STUDENT.id, 12).and(Ge(T_STUDENT.age, 11L).and(In(T_STUDENT.id, Seq(1, 2, 3))).or(Ge(T_STUDENT.age, 15L))))
    val cb = ConditionBuilder()
    println("sql:" + cb.translate(condition))
    println("params:" + cb.params(condition))

    val query = sinq.from(T_STUDENT).join(T_TEACHER).on(Eq(T_STUDENT.teacher, T_TEACHER.id)).where(condition).orderBy(ASC(T_STUDENT.id)).limit(10, 0)

    println("sql:" + query.sql())
    println("params:" + query.params())
  }
}
