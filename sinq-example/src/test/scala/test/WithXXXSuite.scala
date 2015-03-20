package test

import io.sinq.SinqStream
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.JavaConversions._

class WithXXXSuite extends FunSuite with BeforeAndAfter {
  before {
    H2DB.init
  }
  after {
    H2DB.latch.countDown()
  }
  test("withEntityManager") {
    val sinq = SinqStream("h2")
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
}
