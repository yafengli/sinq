package test

import org.koala.sporm.jpa.JPA
import org.specs2._
import javax.persistence.EntityManager
import demo.ii.{CriteriaOperator, CriteriaProcessor, CriteriaComposer, QueryExp}
import models.Book

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class LinkDBSpec extends Specification {
  def init = {
    JPA.initPersistenceName("default")
    "call" must have size (4)
  }

  def is = s2"""

    This is a specification to check the 'Hello world' string

    The 'Hello world' string should
      open db                                           ${H2DB.init}
      init                                              $init
      single                                            $single
      link dsl                                          $link
      close db                                          ${H2DB.close}
    """

  def single = {
    withEntityManager {
      em =>
        val qe = new QueryExp[Book](em)
        val sg = qe.where((cb, cq, from) => cb.equal(from.get("price"), 12)).single()
        val ls = qe.where((cb, cq, from) => cb.gt(from.get("price"), 12)).fetch()
        println(">>>>>>>>>>>>>>>>>>>>>" + sg)
        println(">>>>>>>>>>>>>>>>>>>>>" + ls)
    }
    "Single"
  }

  def link = {
    withEntityManager {
      em =>
        new CriteriaComposer(em,classOf[Book]).where("price", CriteriaOperator.EQUAL, 12).single() match {
          case Some(s) => println(">>>>>>>>>>>>>>>>>>>>>" + s)
          case None => println(">>>>>>>>>>>>>>>>>>>>>Empty.")
        }
    }
    "Link DSL"
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



