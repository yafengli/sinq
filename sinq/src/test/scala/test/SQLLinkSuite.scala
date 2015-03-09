package test

import org.junit.runner.RunWith
import org.koala.sporm.SinqStream
import org.koala.sporm.expression._
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SQLLinkSuite extends FunSuite with BeforeAndAfter {
  before {
  }

  after {
  }
  test("Condition SQL String link.") {
    val condition = Eq("name", "hello").and(Ge("age", 11).or(Ge("id", 5)))
    println("::" + condition.linkCache.toString + "::")
    println("::" + condition.paramsMap + "::")
  }

  test("Default SQL String link.") {
    val sinq = SinqStream()
    val result = sinq.select("f_1", "f_2").from("T_USER").where(Eq("name", "hello").and(Ge("age", 11).or(Ge("id", 5)))).groupBy("f_1").limit(10, 30).orderBy("f_s", "ASC")
    println("::" + result.toSql() + "::")
  }
}


