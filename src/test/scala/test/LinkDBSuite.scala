package test

import org.koala.sporm.jpa.JPA
import javax.persistence.EntityManager
import demo.ii.CriteriaComposer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
@RunWith(classOf[JUnitRunner])
class LinkDBSuite extends FunSuite with BeforeAndAfter {
  before {
    H2DB.open
  }

  after {
    H2DB.close
  }

  //  test("single") {
  //    withEntityManager {
  //      em =>
  //        val qe = new QueryExp[Book](em)
  //        val sg = qe.where((cb, cq, from) => cb.equal(from.get("price"), 12)).single()
  //        val ls = qe.where((cb, cq, from) => cb.gt(from.get("price"), 12)).fetch()
  //        println(">>>>>>>>>>>>>>>>>>>>>" + sg)
  //        println(">>>>>>>>>>>>>>>>>>>>>" + ls)
  //    }
  //  }

  test("link") {
    withEntityManager {
      em =>

      /**
      new CriteriaComposer(em, classOf[Book]).where("price", CriteriaOperator.EQUAL, 999).single() match {
          case Some(s) => println(">>" + s)
          case None => println("##None.")
        }
        */
    }
  }

  private def withEntityManager(call: (EntityManager) => Unit) {
    val em = JPA.createEntityManager()
    try {
      call(em)
    }
    finally {
      em.close()
    }
  }
}



