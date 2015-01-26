package test

import demo.ii.QueryExp
import models.Book
import org.junit.runner.RunWith
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

  test("single") {
    Book.withEntityManager {
      em =>
        val qe = new QueryExp[Book](em)
        val sg = qe.where((cb, cq, from) => cb.equal(from.get("price"), 12)).single()
        val ls = qe.where((cb, cq, from) => cb.gt(from.get("price"), 12)).fetch()
        println(">>>>>>>>>>>>>>>>>>>>>" + sg)
        println(">>>>>>>>>>>>>>>>>>>>>" + ls)
    }
  }

  test("link") {
    Book.withEntityManager {
      em =>

      /**
      new CriteriaComposer(em, classOf[Book]).where("price", CriteriaOperator.EQUAL, Seq(999)).single() match {
          case Some(s) => println(">>" + s)
          case None => println("##None.")
        }
        */
    }
  }
}



