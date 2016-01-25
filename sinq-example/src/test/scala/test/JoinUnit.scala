package test

import gen.{T_STUDENT, T_TEACHER}
import io.sinq.expr._
import io.sinq.func.{ASC, Order}
import test.H2DB._

object JoinUnit {
  val condition = Between(T_STUDENT.id, 1, 12).or(In(T_STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3")))

  def test(): Unit = {
    val query = sinq.select(T_STUDENT.id, T_STUDENT.name, T_TEACHER.id, T_TEACHER.name).from(T_STUDENT).join(T_TEACHER).on(Eq(T_STUDENT.teacher, T_TEACHER.id)).where(condition).orderBy(Order(ASC, T_STUDENT.id)).limit(10, 0)
    println("sql:" + query.sql())
    query.collect().foreach(t => println(s"T:${t}"))
  }
}

