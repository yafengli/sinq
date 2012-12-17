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
    "CriterialQL Test" in {
      test()
    }
  }


  def test() {
    time(() => {
      Book.withEntityManager {
        em => {
          CriteriaQL(em, classOf[Book]).or(cq => {
            var list = List[Predicate]()
            list ::= cq.builder.equal(cq.root.get(Book_.name), "nanjing")
            list ::= cq.builder.equal(cq.root.get(Book_.name), "Shanghai")
            list ::= cq.builder.ge(cq.root.get(Book_.price), 10)
            list ::= cq.builder.le(cq.root.get(Book_.price), 12)
            list
          }).and(cq => {
            var list = List[Predicate]()
            list ::= cq.builder.equal(cq.root.get(Book_.name), "nanjing")
            list ::= cq.builder.equal(cq.root.get(Book_.name), "Shanghai")
            list
          }).fetch(10, 1)
        }
      }
      "CriterialQL Test 1"
    })
  }


  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println("---%s--#time use %sms.".format(name, stop - start))
  }
}
