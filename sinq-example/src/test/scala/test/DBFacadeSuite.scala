package test

import java.util.concurrent.TimeUnit

import models.{Book, Student}
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

import scala.collection.JavaConversions.{bufferAsJavaList, _}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.forkjoin.{ForkJoinTask, RecursiveAction}

class DBFacadeSuite extends FunSuite with BeforeAndAfter {

  before {
    open
  }

  after {
    close
  }

  test("in") {
    val params: java.util.List[Long] = ArrayBuffer(1L, 2L, 3L, 4L)
    val list = facade.withEntityManager {
      em =>
        val query = em.createQuery("select b from Book b where b.id in :ids")
        query.setParameter("ids", params)
        query.getResultList.toList.asInstanceOf[List[Book]]
    }
    println("#list:" + list)
  }

  test("join") {
    val list_2 = facade.fetch(classOf[Book], classOf[Book]) {
      e =>
        Seq(e.==(Seq("student"))("id", 12L))
    }
    println("#join_1:" + list_2)

    val list_3 = facade.fetch(classOf[Book], classOf[Book]) {
      e =>
        val root = e.root.get("student").get("teacher")
        Seq(e.builder.equal(root.get("id"), 13))
    }
    println("#join_2:" + list_3)
  }

  test("count") {
    val count = facade.count(classOf[Book]) {
      e =>
        Array(e.>>("price", 12))
    }
    println("#count:" + count)
  }

  test("single") {
    val single = facade.single(classOf[Book]) {
      e =>
        Array(e.>>("price", 12))
    }
    println("#single:" + single)
  }

  test("fetch") {
    import javax.persistence.Tuple
    val list_1 = facade.fetch(classOf[Book], classOf[Book]) {
      e =>
        Array(e.>>("price", 12))
    }
    println("#fetch_1:" + list_1)

    val list_2 = facade.fetch(classOf[Book], classOf[Tuple]) {
      e =>
        Array(e.>>("price", 12))
    }
    println("#fetch_2:" + list_2)
  }

  test("multi") {
    facade.multi(classOf[Book])(e => {
      Array(e.builder.avg(e.root.get("price")), e.builder.count(e.root.get("id")))
    })(e => {
      Array(e.>>("price", 12))
    }) match {
      case Some(list) =>
        list.foreach {
          t =>
            t.toArray.foreach {
              i =>
                print("#i:" + i + " ")
            }
            println()
        }
        println("#multi:" + list)
      case None =>
    }
  }

  def concurrent(t: Int): String = {
    val task = new FetchAction(t)
    pool.submit(task)
    pool.shutdown()
    pool.awaitTermination(20, TimeUnit.SECONDS)
    "concurrent"
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println(f"---[${name}}][#time use ${stop - start}ms.")
  }
}

case class FetchAction(var count: Int) extends RecursiveAction {

  def compute() {
    if (count == 1) {
      val id = Thread.currentThread().getId
      //facade.fetch(classOf[Student], classOf[Student], 10, 1)(_.!=("name", "test"))
      //facade.get(classOf[Student], 1L)

      val count = facade.count(classOf[Student])(e => {
        Array(e.!=("name", id.toString), e.!=("age", id), e.!=("address", id.toString))
      })
      println(f"#id:${id} count:${count}")
    } else if (count > 1) {
      ForkJoinTask.invokeAll(new FetchAction(count - 1), new FetchAction(1))
    }
  }
}
