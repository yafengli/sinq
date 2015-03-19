package test

import io.sinq.SinqStream
import io.sinq.provider.JPA
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.JavaConversions._

class DBFacadeSuite extends FunSuite with BeforeAndAfter {
  before {
    JPA.initPersistenceName("postgres")
  }
  after {
    JPA.release()
  }

  test("withEntityManager") {
    val sinq = SinqStream("postgres")
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
    println("#list:" + list)
  }
}
