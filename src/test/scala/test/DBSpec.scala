package test

import org.koala.sporm.jpa.{CQExpression, JPA}
import org.specs2.mutable

import javax.persistence.criteria.Predicate
import models.Book
import models.Book_
import models.Teacher
import models.jm.Author
import models.jm.Game
import models.sm.AuthorModel
import models.sm.AuthorModel._
import models.sm.GameModel
import models.sm.GameModel.extendModel
import java.util.Date


/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBSpec extends mutable.Specification {
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
      //      count()
    }
    "Test or and and" in {
      or()
      jm()
    }
  }

  def or() {
    time(() => {
      Teacher.single[Teacher](
        _.or((b, r) => Array(b.ge(r.get("age"), 10), b.le(r.get("age"), 120)))
          .or((b, r) => Array(b.isNotNull(r.get("address")), b.isNotNull(r.get("name"))))
          .or((b, r) => {
          val predicate: Predicate = b.and(Array(b.ge(r.get("age"), 11), b.or(Array(b.le(r.get("age"), 12), b.le(r.get("age"), 13)): _*)): _*)
          Array(b.ge(r.get("age"), 10), predicate)
        })) match {
        case Some(t) => println("t:" + t)
        case None =>
      }
      "Or And"
    })
  }

  def jm() {
    time(() => {
//      val all = GameModel.fetch[Game](t => t.asc("id")).getOrElse(Nil)
//      if (all.size <= 0) {
//        val author = new Author()
//        author.setId(123123)
//        author.setName("123123")
//        author.insert()
//
//        val game = new Game()
//        game.setCreateDate(new Date())
//        game.setName("DiabloIII")
//        game.getAuthors.add(AuthorModel.get(1L).get)
//        game.insert()
//      }
//      else {
//
//      }
//
//      println(f"#size:${all}")
      "Java and CQModel"
    })
  }

  def test() {
    time(() => {
      Book.withEntityManager {
        em => {
          val factory = CQExpression(em, classOf[Book])
          factory.::(Array(
            factory.builder.equal(factory.root.get(Book_.name), "nanjing"),
            factory.builder.le(factory.root.get(Book_.price), 10),
            factory.builder.ge(factory.root.get(Book_.price), 11),
            factory.builder.or(factory.builder.equal(factory.root.get(Book_.name), "nanjing")),
            factory.builder.equal(factory.root.get(Book_.name), "Shanghai"),
            factory.builder.ge(factory.root.get(Book_.price), 12))).fetch(10, 1)
        }
      }
      "withEntityManager"
    })
  }

  def fetch() {
    time(() => {
      Teacher.fetch[Teacher](5, 5) {
        factory =>
          val cab = factory.builder
          val root = factory.root

          val o1 = cab.equal(root.get("name"), "nanjing")
          val o2 = cab.ge(root.get("age"), Integer.valueOf(20))
          val o3 = cab.equal(root.get("id"), Integer.valueOf(1))
          val o4 = cab.notEqual(root.get("address"), "heifei")

          factory.::(Array(cab.or(List(o1, o3): _*), cab.or(List(o2, o4): _*)))
      } match {
        case None =>
        case Some(list) => list.foreach(println(_))
      }
      "Fetch"
    })
  }

  def count() {
    time(() => {
      Book.count[Book] {
        factory =>
          val cab = factory.builder
          val root = factory.root

          val o1 = cab.equal(root.get(Book_.name), "nanjing")
          val o2 = cab.ge(root.get(Book_.price), 20)
          val join = root.join("student")
          val o3 = cab.equal(join.get("age"), 999)

          factory.::(Seq(o1, o2, o3))
      } match {
        case None =>
        case Some(count) => println("#count#:" + count)
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
