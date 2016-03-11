package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.DBInit._

@RunWith(classOf[JUnitRunner])
class BootSuite extends FunSuite with BeforeAndAfter {

  before {
    init()
  }

  after {
    latch.countDown()
  }

  test("Active") {
    ActiveUnit.test
  }
  test("H2") {
    SinqH2Unit.test(sinq_h2)
  }
  test("Postgres") {
    //SinqPGUnit.test(sinq_pg)
  }

  test("Other") {
    Hello.test()
  }
}
