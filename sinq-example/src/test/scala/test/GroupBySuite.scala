package test

import gen.STUDENT
import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.func.Count
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class GroupBySuite extends FunSuite with BeforeAndAfter {
  lazy val sinq = SinqStream("h2")
  before {
    H2DB.init()
  }
  after {
    H2DB.latch.countDown()
  }
  test("Group By.") {
    val condition = Between(STUDENT.id, 1, 12).or(Ge(STUDENT.age, 15L))
    (0 to 5).foreach {
      i =>
        val query = sinq.select(Count(STUDENT.id), Count(STUDENT.name)).from(STUDENT).where(condition).groupBy(STUDENT.id)
        println("sql:"+query.sql())
        query.single() match {
          case Some((id, name)) => println(s"count(id):${id} count(name):${name}")
          case None => println("None")
        }
    }
    (0 to 5).foreach {
      i =>
        val query = sinq.select(Count(STUDENT.id)).from(STUDENT).where(condition).groupBy(STUDENT.id)
        query.single() match {
          case Some(count) => println(s"count:${count}")
          case None => println("None")
        }
    }
  }
}


