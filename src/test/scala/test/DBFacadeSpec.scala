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
      count
      single
      fetch
      multi
      join
      in
    }
    "Concurrent" in {
      time(() => {
        concurrent(2)
        "Sporm fetch"
      })
    }
  }

  /**
   * where in 表达式
   */
  def in {
    val params: java.util.List[Long] = ArrayBuffer(1L, 2L, 3L, 4L)
    val list = facade.withEntityManager {
      em =>
        val query = em.createQuery("select b from Book b where b.id in :ids")
        query.setParameter("ids", params)
        query.getResultList.toList.asInstanceOf[List[Book]]
    }
    println("#list:" + list)
  }

  /**
   * join 表达式
   */
  def join {
    val list_2 = facade.fetch(classOf[Book], classOf[Book]) {
      (_, e) =>
        val pe = e.join[Student]("student", "id", 12) {
          (b, p, v) =>
            b.equal(p, v)
        }
        Array(pe)
    }
    println("#join_1:" + list_2)

    val list_3 = facade.fetch(classOf[Book], classOf[Book]) {
      (_, e) =>
        val root = e.root.get("student").get("teacher")
        Seq(e.builder.equal(root.get("id"), 13))
    }
    println("#join_2:" + list_3)
  }

  def count {
    val count = facade.count(classOf[Book]) {
      (_, e) =>
        Array(e.>>("price", 12))
    }
    println("#count:" + count)
  }

  def single {
    val single = facade.single(classOf[Book]) {
      (_, e) =>
        Array(e.>>("price", 12))
    }
    println("#single:" + single)
  }

  def fetch {
    import javax.persistence.Tuple
    val list_1 = facade.fetch(classOf[Book], classOf[Book]) {
      (_, e) =>
        Array(e.>>("price", 12))
    }
    println("#fetch_1:" + list_1)

    val list_2 = facade.fetch(classOf[Book], classOf[Tuple]) {
      (_, e) =>
        Array(e.>>("price", 12))
    }
    println("#fetch_2:" + list_2)
  }

  def multi {
    val list = facade.multi(classOf[Book])((_, e) => {
      Array(e.builder.avg(e.root.get("price")), e.builder.abs(e.root.get("id")))
    })((_, e) => {
      Array(e.>>("price", 12))
    })

    println("#multi:" + list)
  }

  def concurrent(t: Int) {
    val task = new FetchAction(t)
    pool.submit(task)
    pool.shutdown()
    pool.awaitTermination(20, TimeUnit.SECONDS)

  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println(f"---[${name}}][${DB.size}]--#time use ${stop - start}ms.")
  }
}

case class FetchAction(var count: Int) extends RecursiveAction {

  import DB._

  def compute() {
    if (count > 1) {
      ForkJoinTask.invokeAll(new FetchAction(count - 1), new FetchAction(1))
    }
    else {
      val id = Thread.currentThread().getId
      //facade.fetch(classOf[Student], classOf[Student], 10, 1)(_.!=("name", "test"))
      //facade.get(classOf[Student], 1L)

      val count = facade.count(classOf[Student])((_, e) => {
        Array(e.!=("name", id.toString), e.!=("age", id), e.!=("address", id.toString))
      })
      println(f"#id:${id} size:${size} count:${count}")
      DB.synchronized {
        DB.size += 1
      }
    }
  }
}

object DB {
  val facade = SpormFacade("default")
  val pool = new ForkJoinPool(8)
  var size = 0
}