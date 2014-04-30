package test

import java.util.Date
import javax.persistence.criteria.Predicate
import models.Book
import models.Book_
import models.Teacher
import models.jm.Author
import models.jm.Game
import models.sm.AuthorModel.authorExtend
import models.sm.GameActiveRecord
import models.sm.GameActiveRecord.gameExtend
import org.koala.sporm.jpa.CQExpression
import scala.concurrent.forkjoin.RecursiveAction
import java.util.concurrent.TimeUnit
import DB._
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
@RunWith(classOf[JUnitRunner])
class DBSuite extends FunSuite with BeforeAndAfter {
  before {
    H2DB.init
  }

  after {
    H2DB.close
  }

  test("or") {
    time(() => {
      Teacher.fetch(e => {
        val b = e.builder
        val p1 = b.or(Array(e.>=("age", 10), e.<=("age", 10)): _*)
        val p2 = b.or(Array(e.isNotNull("address"), e.isNotNull("name")): _*)
        val p3_1 = b.and(Array(e.>=("age", 11), b.or(Array(e.<=("age", 12), e.>=("age", 2)): _*)): _*)
        val p3 = b.or(Array(e.<=("age", 13), p3_1): _*)

        Seq(p1, p2, p3)
      }) match {
        case Some(t) => println("t:" + t)
        case None =>
      }
      "Or And"
    })
  }

  test("java model") {
    time(() => {
      GameActiveRecord.fetch(_ => Nil) match {
        case Some(list) =>
          if (list.size <= 0) {
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
          println(f"#size:${list.size}")
        case None =>
      }
      "Java and CQModel"
    })
  }

  test("scala_model") {
    time(() => {
      Book.fetch(_ => Nil) match {
        case Some(list) =>
          if (list.size <= 0) {
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
          println(f"#size:${list.size}")
        case None =>
      }
      "Scala and CQModel"
    })
  }

  test("all_exps") {
    time(() => {
      Book.withEntityManager {
        em => {
          Book.single(e => Seq(e.==("id", 777L)))
          import scala.collection.mutable.ArrayBuffer
          val b = em.getCriteriaBuilder
          val query = b.createQuery(classOf[Book])
          val r = query.from(classOf[Book])
          val e = CQExpression(b, query, r)

          val ps_1 = Array(b.equal(r.get(Book_.name), "nanjing"),
            b.le(r.get(Book_.price), 10),
            b.ge(r.get(Book_.price), 11),
            b.or(b.equal(r.get(Book_.name), "nanjing")),
            b.equal(r.get(Book_.name), "Shanghai"),
            b.ge(r.get(Book_.price), 12))
          val ps_2 = Array(e.>=("price", 12), e.<=("price", 23))

          val ps = ArrayBuffer[Predicate]() ++ ps_1 ++ ps_2

          query.where(ps: _*)
          em.createQuery(query).getResultList
        }
      }
      "withEntityManager"
    })
  }

  test("fetch") {
    time(() => {
      Teacher.fetch(5, 5) {
        e =>
          val o1 = e.==("name", "nanjing")
          val o2 = e.>=("age", 20)
          val o3 = e.==("id", 1)
          val o4 = e.!=("address", "heifei")

          Array(e.builder.or(Seq(o1, o3): _*), e.builder.or(Seq(o2, o4): _*))
      } match {
        case None =>
        case Some(list) => list.foreach(println(_))
      }
      "Fetch"
    })
  }

  test("single") {
    time(() => {
      Book.single(e => Seq(e.==("id", 9))) match {
        case Some(o) => println(o)
        case None =>
      }
      "single"
    })
  }


  test("count") {
    time(() => {
      Book.count {
        e =>
          val cab = e.builder
          val root = e.root

          val o1 = cab.equal(root.get(Book_.name), "nanjing")
          val o2 = cab.ge(root.get(Book_.price), 20)
          val join = root.join("student")
          val o3 = cab.equal(join.get("age"), 999)

          Seq(o1, o2, o3)
      } match {
        case None =>
        case Some(count) => println("#count#:" + count)
      }
      "Fetch count 2"
    })
  }

  test("exp_extend") {
    time(() => {
      Book.single(e => {
        Seq(e.==(Seq("student", "teacher"))("id", 2L))
      }) match {
        case Some(o) => println(f"#book:${o.id} ${o.name}")
        case None => println(f"#BOOK IS NULL.")
      }
      "exp extend"
    })
  }

  def concurrent(t: Int) {
    time(() => {
      val task = new FetchAction(t)
      pool.submit(task)
      pool.shutdown()
      pool.awaitTermination(20, TimeUnit.SECONDS)
      "Concurrent"
    })
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println("---%s--#time use %sms.".format(name, stop - start))
  }
}

case class ModelAction(var count: Int) extends RecursiveAction {

  import DB._

  def compute() {
    if (count == 1) {
      val id = Thread.currentThread().getId
      val count = Book.count(e => Seq(e.>=(Seq("student"))("id", 12)))
      println(f"#id:${id} size:${size} count:${count}")
      DB.synchronized {
        DB.size += 1
      }
    } else if (count > 1) {
      import scala.concurrent.forkjoin.ForkJoinTask
      ForkJoinTask.invokeAll(new ModelAction(count - 1), new ModelAction(1))
    }
  }
}

