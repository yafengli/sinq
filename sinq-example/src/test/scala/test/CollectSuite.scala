package test

import gen._STUDENT
import models.postgres.init.ImplicitsSinq.sinq2Count
import io.sinq.expr._
import io.sinq.func.{ASC, Order}
import io.sinq.provider.Result
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.PGDBInit._

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {
  before {
    init()
  }
  after {
    latch.countDown()
  }

  test("Collect.") {
    val condition = In(_STUDENT.name, Seq("YaFengli:7", "YaFengli:8", "YaFengli:9")).or(Between(_STUDENT.id, 2, 5).and(Ge(_STUDENT.age, 6L)))
    println("1-ount:" + sinq.count(classOf[Student]))
    println("2-count:" + sinq.count(_STUDENT))

    proc(sinq.from(_STUDENT))(_.collect().foreach { t => println(s"id:${t.id} name:${t.name} age:${t.age}") })

    proc(sinq.from(_STUDENT).where(condition).orderBy(Order(ASC, _STUDENT.id)).limit(10, 0))(_.collect().foreach(t => println(s"@id:${t.id} name:${t.name} age:${t.age}")))

    proc(sinq.select(_STUDENT.id, _STUDENT.name, _STUDENT.age).from(_STUDENT).where(condition).orderBy(Order(ASC, _STUDENT.id)).limit(20, 0))(_.collect().foreach {
      case (id, name, age) => println(s"&id:${id} name:${name} age:${age}")
      case _ => println("Error")
    })
    proc(sinq.select(_STUDENT.id, _STUDENT.name).from(_STUDENT).where(Ge(_STUDENT.id, 1)))(_.collect().foreach {
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


