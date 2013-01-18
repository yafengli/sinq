package test

import javax.persistence.criteria.Predicate
import models._
import org.koala.sporm.jpa.{CriteriaQL, JPA}
import org.specs2._
import scala.Some

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
      //      test()
    }

    "CriterialQL Test fetch" in {
      //      fetch()
    }
    "CriterialQL Test count" in {
      count()
    }
  }


  def test() {
    time(() => {
      Book.withEntityManager {
        em => {
          val factory = CriteriaQL(em, classOf[Book])
          factory.and({
            var list = List[Predicate]()
            list ::= factory.builder.equal(factory.root.get(Book_.name), "nanjing")
            list ::= factory.builder.le(factory.root.get(Book_.price), 10)
            list
          }).and({
            var list = List[Predicate]()
            list ::= factory.builder.equal(factory.root.get(Book_.name), "nanjing")
            list ::= factory.builder.ge(factory.root.get(Book_.price), 11)
            list ::= factory.builder.or(factory.builder.equal(factory.root.get(Book_.name), "nanjing"))
            list
          }).and({
            var list = List[Predicate]()
            list ::= factory.builder.equal(factory.root.get(Book_.name), "Shanghai")
            list ::= factory.builder.ge(factory.root.get(Book_.price), 12)
            list
          }).fetch(10, 1)
        }
      }
      "withEntityManager"
    })
  }


  def fetch() {
    time(() => {
      Teacher.fetch(5, 5) {
        factory =>
          val cab = factory.builder
          val root = factory.root

          val o1 = cab.equal(root.get("name"), "nanjing")
          val o2 = cab.ge(root.get("age"), Integer.valueOf(20))
          val o3 = cab.equal(root.get("id"), Integer.valueOf(1))
          val o4 = cab.notEqual(root.get("address"), "heifei")

          factory.::=(List(cab.or(List(o1, o3): _*))).::=(List(cab.or(List(o2, o4): _*)))
      } match {
        case None =>
        case Some(list) => list.foreach(println(_))
      }
      "Fetch"
    })
  }


  def count() {
    time(() => {
      Book.count {
        factory =>
          val cab = factory.builder
          val root = factory.root

          val o1 = cab.equal(root.get(Book_.name), "nanjing")
          val o2 = cab.ge(root.get(Book_.price), 20)

          factory ::= List(o1, o2)
      } match {
        case None =>
        case Some(count) => println("#2#:" + count)
      }
      "Fetch count 2"
    })
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println("---%s--#time use %sms.".format(name, stop - start))
  }
}
