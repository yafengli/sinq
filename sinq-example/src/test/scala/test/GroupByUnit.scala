package test

import java.math.BigInteger

import gen.T_STUDENT
import io.sinq.expr._
import io.sinq.func.Count
import test.H2DB._

object GroupByUnit {
  def test(): Unit = {
    val condition = Between(T_STUDENT.id, 1, 12).or(Ge(T_STUDENT.age, 15L))
    (0 to 5).foreach {
      i =>
        val query = sinq.select(Count[BigInteger](T_STUDENT.id), Count[BigInteger](T_STUDENT.name)).from(T_STUDENT).where(condition).groupBy(T_STUDENT.id)
        println("sql:" + query.sql())
        query.single() match {
          case Some((id, name)) => println(s"count(id):${id} count(name):${name}")
          case None => println("None")
        }
    }
    (0 to 5).foreach {
      i =>
        val query = sinq.select(Count[BigInteger](T_STUDENT.id)).from(T_STUDENT).where(condition).groupBy(T_STUDENT.id)
        query.single() match {
          case Some(count) => println(s"count:${count}")
          case None => println("None")
        }
    }
  }
}


