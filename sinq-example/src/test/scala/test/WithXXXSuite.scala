package test

import models.Teacher
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.PGDBInit._

import scala.collection.JavaConversions._

class WithXXXSuite extends FunSuite with BeforeAndAfter {
  before {
    init
  }
  after {
    latch.countDown()
  }
  test("withEntityManager") {

    val list = sinq.withEntityManager {
      em =>
        val query = em.createNativeQuery("select t.id,t.name,t.age from t_student t where t.id between ? and ? or t.name in (?,?,?,?)")
        query.setParameter(1, -1)
        query.setParameter(2, 12)
        query.setParameter(3, "YaFengLi:1")
        query.setParameter(4, "YaFengLi:2")
        query.setParameter(5, "YaFengLi:3")
        query.setParameter(6, "YaFengLi:4")
        query.getResultList.toList
    }
  }
  test("withTransaction") {
    val list = sinq.withTransaction {
      em =>
        val teacher = Teacher("YaFengLi", 68, "NanJing")
        println(s"id:${teacher.id}")
        sinq.insert(teacher)
        println(s"id:${teacher.id}")

        if (teacher.id != -999) sinq.delete(teacher)
    }
  }
}
