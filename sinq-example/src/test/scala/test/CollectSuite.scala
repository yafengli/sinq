package test

import gen.STUDENT
import init.ImplicitsSinq.sinq2Count
import io.sinq.expr._
import io.sinq.func.{ASC, Order}
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {
  before {
    init()
  }
  after {
    latch.countDown()
  }

  test("Collect.") {
    val condition = In(STUDENT.name, Seq("YaFengli:7", "YaFengli:8", "YaFengli:9")).or(Between(STUDENT.id, 2, 5).and(Ge(STUDENT.age, 6L)))
    println("count:" + sinq.count(classOf[Student]))
    (0 until 1).foreach {
      i =>
        val query = sinq.from(STUDENT)
        println("sql:" + query.sql())
        query.collect().foreach { t => println(s"id:${t.id} name:${t.name} age:${t.age}") }
    }
    (0 until 1).foreach {
      i =>
        val query = sinq.from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
        println("@sql:" + query.sql() + " params:" + query.params())
        query.collect().foreach(t => println(s"@id:${t.id} name:${t.name} age:${t.age}"))
    }
    (0 until 1).foreach {
      i =>
        val q1 = sinq.select(STUDENT.id, STUDENT.name, STUDENT.age).from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(20, 0)
        println("&sql:" + q1.sql())
        q1.collect().foreach {
          case (id, name, age) => println(s"&id:${id} name:${name} age:${age}")
          case _ => println("Error")
        }
        val q2 = sinq.select(STUDENT.id, STUDENT.name).from(STUDENT).where(Ge(STUDENT.id, 1))
        println(">sql:" + q2.sql())
        q2.collect().foreach {
          case (id, name) => println(s">id:${id} name:${name}")
          case _ => println("Error")
        }
    }
  }
}


