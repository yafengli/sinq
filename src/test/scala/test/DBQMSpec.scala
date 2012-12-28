package test

import javax.persistence.criteria.Predicate
import models._
import org.koala.sporm.jpa.{CriteriaQL, JPA}
import org.specs2._

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBQMSpec extends mutable.Specification {
  val init = {
    JPA.initPersistenceName("default")
  }

  "Test all" should {
    "CriterialQL Test withEntityManager" in {
      test()
    }

    "CriterialQL Test fetch" in {
      fetch()
    }
  }


  def test() {
    time(() => {
      Book.withEntityManager {
        em => {
          CriteriaQL(em, classOf[Book]).or(cq => {
            var list = List[Predicate]()
            list ::= cq.cab.equal(cq.root.get(Book_.name), "nanjing")
            list ::= cq.cab.le(cq.root.get(Book_.price), 10)
            list
          }).and(cq => {
            var list = List[Predicate]()
            list ::= cq.cab.equal(cq.root.get(Book_.name), "nanjing")
            list ::= cq.cab.ge(cq.root.get(Book_.price), 11)
            list
          }).or(cq => {
            var list = List[Predicate]()
            list ::= cq.cab.equal(cq.root.get(Book_.name), "Shanghai")
            list ::= cq.cab.ge(cq.root.get(Book_.price), 12)
            list
          }).fetch(10, 1)
        }
      }
      "withEntityManager"
    })
  }


  def fetch() {
    time(() => {
      Book.
      "Fetch"
    })
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println("---%s--#time use %sms.".format(name, stop - start))
  }
}
