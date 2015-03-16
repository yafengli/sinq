package test

import io.sinq.SinqStream
import io.sinq.expression._
import io.sinq.rs.{Count, ASC, Order}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class SingleSuite extends FunSuite with BeforeAndAfter {

  val sinq = SinqStream()
  val condition = Eq(_id, 1).and(Le(_id, 12).or(Ge(_age, 11L).and(In(_id, Seq(1, 2, 3))))).or(Ge(_age, 15L))

  before {
    open
  }

  after {
    close
  }

  test("Single.") {
    val query = sinq.select(_all: _*).from(_table).where(condition)
    query.single() match {
      case Some(Array(id, name)) => println(s"id:${id} name:${name}")
      case None => println("None")
    }

    val query2 = sinq.select(_id).from(_table).where(condition)
    query2.single() match {
      case Some(id) => println(s"id:${id}")
      case None => println("None")
    }
  }
}


