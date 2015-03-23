package test

import java.math.BigInteger

import init.{ADDRESS, USER}
import io.sinq.SinqStream
import io.sinq.expression.{Eq, Ge, In, Le}
import io.sinq.rs.{ASC, Count, Order}
import models.{Address, User}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SinqSuite extends FunSuite with BeforeAndAfter {
  lazy val sinq = SinqStream("h2")
  before {
    H2DB.init()
  }

  after {
    H2DB.latch.countDown()
  }
  test("init.") {
    val count = sinq.select(Count(USER.id)).from(USER).single().getOrElse(BigInteger.valueOf(0))
    if (count.longValue() <= 0) {
      val user = User("user-1", 11)
      sinq.insert(user)
      val address = Address("NanJing", 603)
      address.setUser(user)
      sinq.insert(address)
    }
  }

  test("SQL Build.") {
    val condition = Eq(USER.id, 1).or(Le(USER.id, 12).and(Ge(USER.age, 11L).or(In(USER.id, Seq(1, 2, 3))).or(Ge(USER.age, 15L))))
    val query = sinq.select(USER.id, USER.name, ADDRESS.name, ADDRESS.createDate, ADDRESS.num, ADDRESS.u_id).from(USER).join(ADDRESS).on(Eq(USER.id, ADDRESS.u_id)).where(condition).orderBy(Order(ASC, USER.id)).limit(10, 0)
    println("sql:" + query.sql())
    println("params:" + query.params())
    query.single() match {
      case Some((id, uname, aname, cd, num, u_id)) => println(s"id:${id} name:${uname}:${aname} create:${cd.toString} num:${num} u_id:${u_id}")
      case None => println("Nothing")
    }

    sinq.select(USER).from(USER).join(ADDRESS).on(Eq(USER.id, ADDRESS.u_id)).where(condition).orderBy(Order(ASC, USER.id)).limit(10, 0).single() match {
      case Some(u) => println(s"id:${u.id}")
      case None => println("None")
    }

    sinq.select(USER).from(USER).collect().foreach(u => println(s"id:${u.getId}"))
  }
}

