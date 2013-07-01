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
import models.sm.AuthorModel.authorExtend
import models.sm.GameActiveRecord
import models.sm.GameActiveRecord.gameExtend
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
    "CriterialQL All Expressions withEntityManager" in {
      //      all_exps()
    }

    "CriterialQL Test fetch" in {
      //      fetch()
    }
    "CriterialQL Test count" in {
      //      count()
      count_2()
    }
    "Test or and and" in {
      //or()
      //java_model()
      //scala_model()
    }
  }

  def or() {
    time(() => {
      Teacher.single(
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

  def java_model() {
    time(() => {
      val all = GameActiveRecord.fetch(t => t.asc("id")).getOrElse(Nil)
      if (all.size <= 0) {
        val author = new Author()
        println("#id:" + author.getId)
        author.setName("123123")
        author.insert()
        println("#id:" + author.getId)

        val game = new Game()
        game.setCreateDate(new Date())
        game.setName("DiabloIII")
        game.getAuthors.add(author)
        game.insert()
      }
      println(f"#size:${all.size}")
      "Java and CQModel"
    })
  }

  def scala_model() {
    time(() => {
      val all = Book.fetch(t => t.asc("id")).getOrElse(Nil)
      if (all.size <= 0) {
        import models.Student
        val student = Student("student", 12, "address")
        println("#sid:" + student.id)
        student.insert()
        println("#sid:" + student.id)

        val book = Book("book", 999)
        book.student = student
        println("#bid:" + book.id)
        book.insert()
        println("#bid:" + book.id)
      }
      println(f"#size:${all.size}")
      "Scala and CQModel"
    })
  }

  def all_exps() {
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
      Teacher.fetch(5, 5) {
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
      Book.count {
        factory =>
          val cab = factory.builder
          val root = factory.tupleRoot

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

  def count_2() {
    time(() => {
      Book.count_2 {
        (b, q, r) =>
          val o1 = b.equal(r.get(Book_.name), "nanjing")
          val o2 = b.ge(r.get(Book_.price), 20)
          val join = r.join("student")
          val o3 = b.equal(join.get("age"), 999)

          Seq(o1, o2, o3)
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
