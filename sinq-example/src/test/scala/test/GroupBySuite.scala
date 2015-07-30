package test

import gen._STUDENT
import io.sinq.expr._
import io.sinq.func.Count
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class GroupBySuite extends FunSuite with BeforeAndAfter {
  before {
    init()
  }
  after {
    latch.countDown()
  }
  test("Group By.") {
    val condition = Between(_STUDENT.id, 1, 12).or(Ge(_STUDENT.age, 15L))
    (0 to 5).foreach {
      i =>
        val query = sinq.select(Count(_STUDENT.id), Count(_STUDENT.name)).from(_STUDENT).where(condition).groupBy(_STUDENT.id)
        println("sql:" + query.sql())
        query.single() match {
          case Some((id, name)) => println(s"count(id):${id} count(name):${name}")
          case None => println("None")
        }
    }
    (0 to 5).foreach {
      i =>
        val query = sinq.select(Count(_STUDENT.id)).from(_STUDENT).where(condition).groupBy(_STUDENT.id)
        query.single() match {
          case Some(count) => println(s"count:${count}")
          case None => println("None")
        }
    }
  }
}


