package test

import org.koala.sporm.jpa.JPA
import org.specs2._
import javax.persistence.EntityManager
import demo.ii.QueryExp
import models.Book


/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBIISpec extends Specification {
  def init = {

    "call" must have size (4)
  }

  def is = s2"""

 This is a specification to check the 'Hello world' string

 The 'Hello world' string should
   single                                            $single
                                                     """

  def single = {
    H2DB.init
    JPA.initPersistenceName("default")
    withEntityManager {
      em =>
        val qe = new QueryExp[Book](em)
        val sl = qe.where((cb, cq, from) => cb.equal(from.get("price"), 12)).single()
        println(sl)
    }
    H2DB.close
    "Single"
  }

  def withEntityManager(call: (EntityManager) => Unit) {
    val em = JPA.createEntityManager()
    try {
      call(em)
    }
    finally {
      em.close()
    }
  }
}



