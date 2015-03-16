package test

import io.sinq.SinqStream
import io.sinq.expression._
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
    val query = sinq.select(_all: _*).from(_table).where(condition) //.groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)
    query.single() match {
      case Some(Array(id, name)) => println(s"id:${id} name:${name}")
      case None => println("None")
    }
  }
}


