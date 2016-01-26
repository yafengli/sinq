package test

import gen.T_STUDENT
import io.sinq.expr._
import io.sinq.func.ASC
import io.sinq.provider.Result
import models.Student
import models.postgres.init.ImplicitsSinq.sinq2Count
import test.H2DB._

object CollectUnit {
  def test(): Unit = {
    val condition = In(T_STUDENT.name, Seq("YaFengli:7", "YaFengli:8", "YaFengli:9")).or(Between(T_STUDENT.id, 2, 5).and(Ge(T_STUDENT.age, 6L)))
    println("1-ount:" + sinq.count(classOf[Student]))
    println("2-count:" + sinq.count(T_STUDENT))

    proc(sinq.from(T_STUDENT))(_.collect().foreach { t => println(s"id:${t.id} name:${t.name} age:${t.age}") })

    proc(sinq.from(T_STUDENT).where(condition).orderBy(ASC(T_STUDENT.id)).limit(10, 0))(_.collect().foreach(t => println(s"@id:${t.id} name:${t.name} age:${t.age}")))

    proc(sinq.select(T_STUDENT.id, T_STUDENT.name, T_STUDENT.age).from(T_STUDENT).where(condition).orderBy(ASC(T_STUDENT.id)).limit(20, 0))(_.collect().foreach {
      case (id, name, age) => println(s"&id:${id} name:${name} age:${age}")
      case _ => println("Error")
    })
    proc(sinq.select(T_STUDENT.id, T_STUDENT.name).from(T_STUDENT).where(Ge(T_STUDENT.id, 1)))(_.collect().foreach {
      case (id, name) => println(s">id:${id} name:${name}")
      case _ => println("Error")
    })
  }

  private def proc[T](query: Result[T])(call: (Result[T]) => Unit): Unit = {
    (0 until 1).foreach {
      i =>
        println(s"sql:${query.sql()}")
        println(s"parasm:${query.params}")
        call(query)
    }
  }
}


