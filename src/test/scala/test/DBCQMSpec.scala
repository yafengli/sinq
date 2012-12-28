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
class DBCQMSpec extends mutable.Specification {
  val init = {
    JPA.initPersistenceName("default")
  }

  "Test all" should {
    "CriterialQL Test withEntityManager" in {
      test()
    }

    "CriterialQL Test fetch" in {
      //      fetch()
    }
  }


  def test() {
    time(() => {
      Book.withEntityManager {
        em => {
          val factory = CriteriaQL(em, classOf[Book])
          factory.or({
            var list = List[Predicate]()
            list ::= factory.cab.equal(factory.root.get(Book_.name), "nanjing")
            list ::= factory.cab.le(factory.root.get(Book_.price), 10)
            list
          }).or({
            var list = List[Predicate]()
            list ::= factory.cab.equal(factory.root.get(Book_.name), "nanjing")
            list ::= factory.cab.ge(factory.root.get(Book_.price), 11)
            list ::= factory.cab.or(factory.cab.equal(factory.root.get(Book_.name), "nanjing"))
            list
          }).or({
            var list = List[Predicate]()
            list ::= factory.cab.equal(factory.root.get(Book_.name), "Shanghai")
            list ::= factory.cab.ge(factory.root.get(Book_.price), 12)
            list
          }).fetch(10, 1)
        }
      }
      "withEntityManager"
    })
  }


  def fetch() {
    time(() => {
      Book.fetch(5, 5) {
        factory =>

          val list = List[Predicate]()
          val cab = factory.cab
          val root = factory.root

          val o1 = cab.equal(root.get("name"), "nanjing")
          val o2 = cab.ge(root.get("price"), Integer.valueOf(20))
          val o3 = cab.equal(root.get("price"), Integer.valueOf(30))
          val o4 = cab.notEqual(root.get("name"), "heifei")
          //        val c = cab.or(o1, cab.or(o2, o3), o4)
          factory.or(List(o1)).or(List(o1, o2)).or(List(o4))
      } match {
        case None =>
        case Some(list) => list.foreach(println(_))
      }
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
