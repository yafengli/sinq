package test

import init.{ADDRESS, USER}
import io.sinq.SinqStream
import io.sinq.expression.{Eq, Ge, In, Le}
import io.sinq.provider.JPA
import io.sinq.rs.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SinqSuite extends FunSuite with BeforeAndAfter {

  before {
    JPA.initPersistenceName("h2", "postgres")
  }
  after {
    JPA.release()
  }

  test("SQL Build.") {
    val sinq = SinqStream("h2")
    val condition = Eq(USER.id, 1).or(Le(USER.id, 12).and(Ge(USER.age, 11L).or(In(USER.id, Seq(1, 2, 3))).or(Ge(USER.age, 15L))))
    println("sql:" + condition.translate())
    println("params:" + condition.params())

    val query = sinq.select(USER.id).from(USER).join(ADDRESS).on(Eq(USER.id, ADDRESS.u_id)).where(condition).orderBy(Order(ASC, USER.id)).limit(10, 0)

    println("sql:" + query.sql())
    println("params:" + query.params())
    println("single:" + query.single())
  }
}
