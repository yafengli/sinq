package test

import io.sinq.SinqStream
import models.Book
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

class DBFacadeSuite extends FunSuite with BeforeAndAfter {
  val sinq = SinqStream()
  before {
    H2DB.open
  }

  after {
    H2DB.close
  }

  test("withEntityManager") {
    val params = ArrayBuffer(1L, 2L, 3L, 4L)
    val list = sinq.withEntityManager {
      em =>
        val query = em.createQuery("select b from Book b where b.id in :ids")
        query.setParameter("ids", params)
        query.getResultList.toList.asInstanceOf[List[Book]]
    }
    println("#list:" + list)
  }
}
