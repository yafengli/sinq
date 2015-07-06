package test

import init.{_ADDRESS, _USER}
import io.sinq.SinqStream
import io.sinq.expression.{Eq, Ge, In, Le}
import io.sinq.func.{ASC, Count, Order, _}
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

  test("SQL Build.") {
    val condition = Eq(_USER.id, 1).or(Le(_USER.id, 12).and(Ge(_USER.age, 11L).or(In(_USER.id, Seq(1, 2, 3))).or(Ge(_USER.age, 15L))))
    val q1 = sinq.select(_USER.id, _USER.name, _ADDRESS.name, _ADDRESS.createDate, _ADDRESS.num, _ADDRESS.u_id).from(_USER).join(_ADDRESS).on(Eq(_USER.id, _ADDRESS.u_id)).where(condition).orderBy(Order(ASC, _USER.id)).limit(10, 0)
    println("sql:" + q1.sql())
    println("params:" + q1.params())
    q1.single() match {
      case Some((id, uname, aname, cd, num, u_id)) => println(s"id:${id} name:${uname}:${aname} create:${cd.toString} num:${num} u_id:${u_id}")
      case None => println("None")
    }
    sinq.select(_USER.id).from(_USER).join(_ADDRESS).on(Eq(_USER.id, _ADDRESS.u_id)).where(condition).orderBy(Order(ASC, _USER.id)).limit(10, 0).single() match {
      case Some(id) => println(s"[id]:${id}")
      case None => println("None")
    }
    sinq.from(_USER).join(_ADDRESS).on(Eq(_USER.id, _ADDRESS.u_id)).where(condition).orderBy(Order(ASC, _USER.id)).limit(10, 0).single() match {
      case Some(u) => println(s"[User] id:${u.getId} name:${u.getName} age:${u.getAge} address:${u.getAddress.getName}")
      case None => println("None")
    }
    sinq.select(_USER.id, _USER.name, _USER.age).from(_USER).collect().foreach(t => println(s"[id,name,age] id:${t._1} name:${t._2} age:${t._3}"))
    sinq.from(_USER).collect().foreach(u => println(s"[User] id:${u.getId} name:${u.getName} age:${u.getAge} address:${u.getAddress.getName}"))
    sinq.select(Count(_USER.id)).from(_USER).single().foreach(c => println(s"count:${c}"))
    sinq.select(Sum(_USER.age)).from(_USER).single().foreach(s => println(s"sum:${s}"))
  }
}

