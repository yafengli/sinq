package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

@RunWith(classOf[JUnitRunner])
class BootSuite extends FunSuite with BeforeAndAfter {
  before {
    init()
  }
  after {
    latch.countDown()
  }
  test("Join.") {
    JoinUnit.test()
  }
  test("Collection.") {
    CollectUnit.test()
  }
  test("GroupBy.") {
    GroupByUnit.test()
  }
  test("Single.") {
    SingleUnit.test()
  }
  test("WithXXX.") {
    WithXXXUnit.testWithEntityManager()
    WithXXXUnit.testWithTransaction
  }
}

