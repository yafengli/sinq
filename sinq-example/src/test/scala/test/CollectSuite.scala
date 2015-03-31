package test

import init.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.func.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {
  lazy val sinq = SinqStream("h2")
  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }

  test("Collect.") {
    val condition = Between(STUDENT.id, -1, 12).and(Ge(STUDENT.age, 5L).or(In(STUDENT.name, Seq("YaFengli:0", "YaFengli:1", "YaFengli:2", "YaFengli:3"))))
    (0 to 1).foreach {
      i =>
        val query = sinq.from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
        query.collect().foreach(t => println(s"#id:${t.id} name:${t.name} age:${t.age}"))
    }
    (0 to 1).foreach {
      i =>
        val query = sinq.select(STUDENT.id, STUDENT.name, STUDENT.age).from(STUDENT).where(condition).orderBy(Order(ASC, STUDENT.id)).limit(10, 0)
        query.collect().foreach {
          case (id, name, age) => println(s">id:${id} name:${name} age:${age}")
          case _ => println("Error")
        }
        println("#########################")
        sinq.select(STUDENT.id, STUDENT.name).from(STUDENT).where(Ge(STUDENT.id, 1)).collect().foreach {
          case (id, name) => println(s"id:${id} name:${name}")
          case _ => println("Error")
        }
        println("#########################")
        sinq.select(STUDENT.id).from(STUDENT).where(Ge(STUDENT.id, 1)).collect().foreach { id => println(s"id:${id}") }
    }
  }
}


