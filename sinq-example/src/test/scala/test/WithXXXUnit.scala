package test

import models.Teacher
import test.H2DB._

import scala.collection.JavaConversions._

object WithXXXUnit {
  def testWithEntityManager(): Unit = {
    val list = sinq_pg.withEntityManager {
      em =>
        val query = em.createNativeQuery("select t.id,t.name,t.age from e_student t where t.id between ? and ? or t.name in (?,?,?,?)")
        query.setParameter(1, -1)
        query.setParameter(2, 12)
        query.setParameter(3, "YaFengLi:1")
        query.setParameter(4, "YaFengLi:2")
        query.setParameter(5, "YaFengLi:3")
        query.setParameter(6, "YaFengLi:4")
        query.getResultList.toList
    }
  }

  def testWithTransaction: Unit = {
    val list = sinq_pg.withTransaction {
      em =>
        val teacher = Teacher("YaFengLi", 68, "NanJing")
        println(s"id:${teacher.id}")
        sinq_pg.insert(teacher)
        println(s"id:${teacher.id}")

        if (teacher.id != -999) sinq_pg.delete(teacher)
    }
  }
}
