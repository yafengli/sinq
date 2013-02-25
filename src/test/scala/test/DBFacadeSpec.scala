package test

import models._
import org.koala.sporm.jpa.SpormFacade
import org.specs2._
import concurrent.forkjoin.{ForkJoinPool, RecursiveAction, ForkJoinTask}
import java.util.concurrent.TimeUnit

class DBFacadeSpec extends mutable.Specification {

  import DB._

  "SpormFacade test all" should {

    "Sporm test" in {
      //      test()
      testFetch()
    }
  }

  def testFetch() {
    time(() => {
      facade.fetch(classOf[Student], 10, 1)(_.==("name", "test"))
      "Sporm fetch"
    })
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

object DB {
  val facade = SpormFacade("default")
  val pool = new ForkJoinPool(8)
  var size = 0
}