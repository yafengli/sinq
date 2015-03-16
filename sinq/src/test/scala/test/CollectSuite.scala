package test

import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.{ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class CollectSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()
  val condition = Between(_id, -1, 12).and(Ge(_age, 11L).or(In(_id, Seq(1, 2, 3, 4, 5))))

  before {
    open
  }

  after {
    close
  }

  test("Collect.") {
    val query = sinq.select(_all: _*).from(_table).where(condition).orderBy(Order(ASC, _all: _*)).limit(10, 0)
    query.collect(classOf[Array[Any]]).foreach {
      case Array(id, name) => println(s"#id:${id} name:${name}")
      case _ =>
    }
  }
}


