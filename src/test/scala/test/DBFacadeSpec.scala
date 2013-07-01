package test

import concurrent.forkjoin.{ForkJoinPool, RecursiveAction, ForkJoinTask}
import java.util.concurrent.TimeUnit
import models._
import org.koala.sporm.jpa.SpormFacade
import org.specs2._
import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

class DBFacadeSpec extends mutable.Specification {

  import DB._

  "SpormFacade test all" should {

    "Sporm test" in {
      //      test()
      testFetch()
    }
  }

  def init() {
    if (Book.count[Book](_.asc("id")).getOrElse(-1L) <= 0) {
      val student = Student("test1", 12, "nanjing")
      println("#id:" + student.id)
      val s = student.insert().get
      println("#id:" + student.id)
      val book = Book("test", 123)
      book.student = s
      book.insert()
    }
  }

  def testFetch() {
    time(() => {
      //init()
      fetch
      //      join
      //      collection
      //multi
      //      val list_2 = Book.multi[Book]((cb, root) => Array(cb.avg(root.get("id")), cb.sum(root.get("id"))))(_.!=("id", -1L))
      //      println("#list:" + list_2)
      "Sporm fetch"
    })
  }

  def collection {
    val params: java.util.List[Long] = ArrayBuffer(1L, 2L, 3L, 4L)
    val list = facade.withEntityManager {
      em =>
        val query = em.createQuery("select b from Book b where b.id in :ids")
        query.setParameter("ids", params)
        query.getResultList.toList.asInstanceOf[List[Book]]
    }
    println("#list:" + list)
  }

  def fetch {
    val task = new FetchAction(20)
    pool.submit(task)
    pool.shutdown()
    pool.awaitTermination(20, TimeUnit.SECONDS)

  }

  def join {
    val list_2 = facade.fetch(classOf[Book], classOf[Book])(_.join[Student]("student", "id", 12)((b, p, v) => {
      b.equal(p, v)
    }))
    println("#list_2:" + list_2)

    val list_3 = facade.fetch(classOf[Book], classOf[Book])(f => {
      val builder = f.builder
      val root = f.root.get("student").get("teacher")
      f.::(builder.equal(root.get("id"), 13))
    })

    println("#list_3:" + list_3)
  }

  def multi {
    Book.withEntityManager {
      em =>
        val cb = em.getCriteriaBuilder
        //          val cq = cb.createQuery(classOf[Book])
        val cq = cb.createTupleQuery()
        val root = cq.from(classOf[Book])
        cq.multiselect(Array(cb.avg(root.get("id")), cb.sum(root.get("id"))): _*)

        //        cq.where(Array(cb.greaterThan(root.get[Long]("id"), 0L)): _*)

        val list = em.createQuery(cq).getResultList
        list.foreach {
          t =>
            val buffer = ArrayBuffer[String]()
            t.toArray.foreach(buffer += _.toString)

            print(buffer.mkString(","))
            println("#######")
        }
        println("#list:" + list.size())
    }
  }

  def test = {
    time(() => {
      val task = new SqlAction(5000)
      pool.submit(task)
      pool.shutdown()
      pool.awaitTermination(20, TimeUnit.SECONDS)
      "Sporm test"
    })
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println(f"---[${name}}][${DB.size}]--#time use ${stop - start}ms.")
  }
}

case class SqlAction(var count: Int) extends RecursiveAction {

  import DB._

  def compute() {
    if (count > 1) {
      ForkJoinTask.invokeAll(new SqlAction(count - 1), new SqlAction(1))
    }
    else {
      facade.get(classOf[Student], 1L)
      facade.count(classOf[Student])(_.!=("name", "123").<<("age", 12))
      size += 1
    }
  }
}

case class FetchAction(var count: Int) extends RecursiveAction {

  import DB._

  def compute() {
    if (count > 1) {
      ForkJoinTask.invokeAll(new FetchAction(count - 1), new FetchAction(1))
    }
    else {
      //facade.fetch(classOf[Student], classOf[Student], 10, 1)(_.!=("name", "test"))
      //facade.get(classOf[Student], 1L)
      val id = Thread.currentThread().getId
      val count = facade.count(classOf[Student])(_.!=("name", id.toString).!=("age", id).!=("address", id.toString))
      println(f"#id:${id} size:${size} count:${count}")
      size += 1
    }
  }
}

object DB {
  val facade = SpormFacade("default")
  val pool = new ForkJoinPool(8)
  var size = 0
}