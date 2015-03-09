package test

import models.Student
import org.junit.runner.RunWith
import org.koala.sporm.SinqStream
import org.koala.sporm.expression.{Eq, Ge}
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
@RunWith(classOf[JUnitRunner])
class DBBasicSuite extends FunSuite with BeforeAndAfter {
  before {
    H2DB.open
  }

  after {
    H2DB.close
  }

  test("Default SQL String link.") {
    val sinq = SinqStream()
    val count = sinq.count(classOf[Student])
    if (count <= 0) {
      val student = Student("YaFengLi", 12, "NanJing 1999.")
      //      sinq.insert(student)
    }

    val result = sinq.select("id", "name").from("t_student").where(Eq("name", "YaFengLi").and(Ge("age", 11).or(Ge("id", -1)))).orderBy("id", "ASC").limit(10, 0)

    println(s"##count:${count}##")
    println("::" + result.sql() + "::")
    println("::" + result.params() + "::")
    val t = result.single()
    println("::" + t.getClass + "::")
  }
}



