package test

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
          factory.::=(
            factory.builder.equal(factory.root.get(Book_.name), "nanjing"),
            factory.builder.le(factory.root.get(Book_.price), 10),
            factory.builder.ge(factory.root.get(Book_.price), 11),
            factory.builder.or(factory.builder.equal(factory.root.get(Book_.name), "nanjing")),
            factory.builder.equal(factory.root.get(Book_.name), "Shanghai"),
            factory.builder.ge(factory.root.get(Book_.price), 12)
          ).fetch(10, 1)
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

          factory.::=(cab.or(List(o1, o3): _*), cab.or(List(o2, o4): _*))
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

          factory ::=(o1, o2)
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
