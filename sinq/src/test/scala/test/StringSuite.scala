package test

import init.{ADDRESS, USER}
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
    val condition = Eq(USER.id, 1).or(Le(USER.id, 12).and(Ge(USER.age, 11L).and(In(USER.id, Seq(1, 2, 3))).or(Ge(USER.age, 15L))))
    println("sql:" + condition.translate())
    println("params:" + condition.params())

    val query = sinq.select().from(USER).join(ADDRESS).on(Eq(USER.id, ADDRESS.u_id)).where(condition).orderBy(Order(ASC, USER.id)).limit(10, 0)

    println("sql:" + query.sql())
    println("params:" + query.params())
  }
}
