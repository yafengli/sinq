package test

import init.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.{ASC, Order}
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {
  val condition = Between(STUDENT.id, -1, 12).and(Ge(STUDENT.age, 5L).or(In(STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3"))))

  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }

  test("Collect.") {
    val sinq = SinqStream("h2")
    (0 to 1).foreach {
      i =>
        val query = sinq.select().from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
        query.collect(classOf[Student]).foreach(t => println(s"#id:${t.id} name:${t.name} age:${t.age}"))
    }
    (0 to 1).foreach {
      i =>
        val query = sinq.select(STUDENT.* : _*).from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
        query.collect().foreach {
          case Array(id, name, age) => println(s">id:${id} name:${name} age:${age}")
        }
        println("#########################")
        sinq.select(STUDENT.id, STUDENT.name).from(STUDENT).where(Ge(STUDENT.id, 1)).collect().foreach { case Array(id, name) => println(s"id:${id} name:${name}") }
        println("#########################")
        sinq.select(STUDENT.id).from(STUDENT).where(Ge(STUDENT.id, 1)).collect().foreach { id => println(s"id:${id}") }
    }
  }
}


