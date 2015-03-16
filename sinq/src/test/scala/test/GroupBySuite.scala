package test

import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.Count
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class GroupBySuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()
  val condition = Between(_id, 1, 12).or(Ge(_age, 15L))

  before {
    open
  }

  after {
    close
  }

  test("Group By.") {
    val query = sinq.select(Count(_id), Count(_name)).from(_table).where(condition).groupBy(_id)
    query.single() match {
      case Some(Array(id, name)) => println(s"id:${id} name:${name}")
      case None => println("None")
    }

    val query2 = sinq.select(Count(_id)).from(_table).where(condition).groupBy(_id)
    query2.single() match {
      case Some(count) => println(s"count:${count}")
      case None => println("None")
    }
  }
}


