package test

import org.junit.runner.RunWith
import org.koala.sporm.expression._
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SQLLinkSuite extends FunSuite with BeforeAndAfter {
  before {
  }

  after {
  }
  test("SQL String link.") {
    val condition = Eq("name", "hello").and(Ge("age", 11).or(Ge("id", 5)))
    println("::" + condition.linkCache.toString + "::")
    println("::" + condition.paramsMap + "::")
  }
}


