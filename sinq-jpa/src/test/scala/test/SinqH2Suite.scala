package test

import io.sinq.expr.{Eq, Ge, In, Le}
import io.sinq.func.{ASC, Count, Order, Sum}
import models.h2.init._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DBInit._

@RunWith(classOf[JUnitRunner])
class SinqH2Suite extends FunSuite with BeforeAndAfter {

  before {
    init()
  }

  after {
    latch.countDown()
  }

  test("SQL Build.") {
    val condition = Eq(T_PERSON.id, 1).or(Le(T_PERSON.id, 12).and(Ge(T_PERSON.age, 11L).or(In(T_PERSON.id, Seq(1, 2, 3))).or(Ge(T_PERSON.age, 15L))))
    val q1 = sinq.select(T_PERSON.id, T_PERSON.name, T_ZONE.name, T_ZONE.createDate, T_ZONE.p_id).from(T_PERSON).join(T_ZONE).on(Eq(T_PERSON.id, T_ZONE.p_id)).where(condition).orderBy(Order(ASC, T_PERSON.id)).limit(10, 0)
    println("sql:" + q1.sql())
    println("params:" + q1.params())
    q1.single() match {
      case Some((id, un, an, cd, p_id)) => println(s"id:${id} name:${un}:${an} create:${cd.toString} p_id:${p_id}")
      case None => println("None")
    }
    sinq.select(T_PERSON.id).from(T_PERSON).join(T_ZONE).on(Eq(T_PERSON.id, T_ZONE.p_id)).where(condition).orderBy(Order(ASC, T_PERSON.id)).limit(10, 0).single() match {
      case Some(id) => println(s"[id]:${id}")
      case None => println("None")
    }
    sinq.from(T_PERSON).join(T_ZONE).on(Eq(T_PERSON.id, T_ZONE.p_id)).where(condition).orderBy(Order(ASC, T_PERSON.id)).limit(10, 0).single() match {
      case Some(p) => println(s"[Person] id:${p.id} name:${p.name} age:${p.age} address:${p.zone}")
      case None => println("None")
    }

    sinq.select(T_PERSON.id, T_PERSON.name, T_PERSON.age).from(T_PERSON).collect().foreach(t => println(s"[id,name,age] id:${t._1} name:${t._2} age:${t._3}"))
    sinq.from(T_PERSON).collect().foreach(u => println(s"[Person] id:${u.id} name:${u.name} age:${u.age} address:${u.zone}"))
    sinq.select(Count(T_PERSON.id)).from(T_PERSON).single().foreach(c => println(s"count:${c}"))
    sinq.select(Sum(T_PERSON.age)).from(T_PERSON).single().foreach(s => println(s"sum:${s}"))
  }
}

