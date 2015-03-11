package test

import models.Student
import org.junit.runner.RunWith
import org.koala.sporm.SinqStream
import org.koala.sporm.expression.{Eq, Ge, In}
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

  test("SQL Build.") {
    val condition = Eq("name", "YaFengLi").and(Ge("age", 11).or(Ge("id", -1)).or(In("id", Set(1L, 2L, 3L))))
    println(condition.linkCache.toString)
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
    //println("::" + result.sql() + "::")
    //println("::" + result.params() + "::")

    result.single() match {
      case Array(id, name) => println(s"id:${id} name:${name}")
      case _ => println("None")
    }
    val result2 = sinq.select().from("t_student").where(Eq("name", "YaFengLi").and(Ge("age", 11).or(Ge("id", -1)))).orderBy("id", "ASC").limit(10, 0)
    result2.single(classOf[Student]) match {
      case Some(t) => println(s"T:${t}")
      case None => println("None")
    }
  }
}