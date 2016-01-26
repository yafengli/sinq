package test

import io.sinq.SinqStream
import io.sinq.expr.{Eq, Ge, In, Le}
import io.sinq.func._
import models.postgres.Address
import models.postgres.init._

import scala.collection.JavaConversions._

object SinqPGUnit {
  def test(implicit sinq: SinqStream): Unit = {
    val condition = Eq(T_USER.id, 1).or(Le(T_USER.id, 12).and(Ge(T_USER.age, 11L).or(In(T_USER.id, Seq(1, 2, 3))).or(Ge(T_USER.age, 15L))))
    val q1 = sinq.select(T_USER.id, T_USER.name, T_ADDRESS.name, T_ADDRESS.createDate, T_ADDRESS.num, T_ADDRESS.u_id)
      .from(T_USER).join(T_ADDRESS).on(Eq(T_USER.id, T_ADDRESS.u_id)).where(condition).orderBy(ASC(T_USER.id)).limit(10, 0)
    println("sql:" + q1.sql())
    println("params:" + q1.params())
    q1.single() match {
      case Some((id, un, an, cd, num, u_id)) => println(s"id:${id} name:${un}:${an} create:${cd.toString} num:${num} u_id:${u_id}")
      case None => println("None")
    }
    sinq.select(T_USER.id).from(T_USER).join(T_ADDRESS).on(Eq(T_USER.id, T_ADDRESS.u_id)).where(condition).orderBy(ASC(T_USER.id)).limit(10, 0).single() match {
      case Some(id) => println(s"[id]:${id}")
      case None => println("None")
    }
    sinq.from(T_USER).join(T_ADDRESS).on(Eq(T_USER.id, T_ADDRESS.u_id)).where(condition).orderBy(ASC(T_USER.id)).limit(10, 0).single() match {
      case Some(u) => println(s"[User] id:${u.getId} name:${u.getName} age:${u.getAge} address:${u.getAddress}")
      case None => println("None")
    }

    sinq.withEntityManager { em =>
      val q = em.createNativeQuery("select ipaddr from t_address")
      q.getResultList.toIndexedSeq.foreach(println(_))
    }

    sinq.withEntityManager { em =>
      val q = em.createNativeQuery("select t.* from t_address as t where t.ipaddr = ?::inet", classOf[Address])
      q.setParameter(1, "192.168.0.232")
      q.getResultList.toIndexedSeq.foreach(println(_))
    }

    sinq.select(T_USER.id, T_USER.name, T_USER.age).from(T_USER).collect().foreach(t => println(s"[id,name,age] id:${t._1} name:${t._2} age:${t._3}"))
    sinq.from(T_USER).collect().foreach(u => println(s"[user] id:${u.getId} name:${u.getName} age:${u.getAge} address:${u.getAddress}"))
    sinq.select(Count[Long](T_USER.id)).from(T_USER).single().foreach(c => println(s"[count]:${c}"))
    sinq.select(Sum[Long](T_USER.age)).from(T_USER).single().foreach(s => println(s"[sum]:${s}"))
    sinq.select(Avg[java.math.BigDecimal](T_USER.age)).from(T_USER).single().foreach(s => println(s"[avg]:${s}"))
  }
}

