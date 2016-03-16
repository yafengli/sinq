package test

import java.sql.{Connection, DriverManager}

import gen.{T_STUDENT, T_TEACHER}
import io.sinq.expr.{Eq, Ge, Le}
import io.sinq.func.ASC
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

import scala.collection.mutable.Buffer

@RunWith(classOf[JUnitRunner])
class BootSuite extends FunSuite with BeforeAndAfter {
  before {
    init()
  }
  after {
    latch.countDown()
  }
  test("Join.") {
    //    JoinUnit.test()
  }
  test("Collection.") {
    //    CollectUnit.test()
  }
  test("GroupBy.") {
    //    GroupByUnit.test()
  }
  test("Single.") {
    //    SingleUnit.test()
  }
  test("WithXXX.") {
    //    WithXXXUnit.testWithEntityManager()
    //    WithXXXUnit.testWithTransaction
  }

  test("Time") {
    val test_count = 20000
    val condition = Eq(T_STUDENT.id, 1).or(Le(T_STUDENT.id, 9L).and(Ge(T_STUDENT.age, 11L).and(Le(T_STUDENT.age, 15L))))

    val start = System.currentTimeMillis()
    println(s"sinq:${sinq_h2}")
    val end = System.currentTimeMillis()
    println(s"time:${end - start}ms.")

    time("ALL", test_count) {
      sinq_h2.from(T_STUDENT).join(T_TEACHER).on(Eq(T_STUDENT.teacher, T_TEACHER.id)).where(condition).orderBy(ASC(T_STUDENT.id)).limit(10, 0).single()
    }

    val query = sinq_h2.from(T_STUDENT).join(T_TEACHER).on(Eq(T_STUDENT.teacher, T_TEACHER.id)).where(condition).orderBy(ASC(T_STUDENT.id)).limit(10, 0)

    time("sql builder", test_count) {
      query.sql()
      query.params()
    }

    val buffer = Buffer[Any]()
    time("query", test_count) {
      query.single()
    }

    val sql = query.sql()
    val params = query.params()
    println(s"sql:${query.sql()}")
    println(s"params:${query.params()}")

    time("jdbc", test_count) {
      jdbc() match {
        case Some(conn) =>
          val stmt = conn.prepareStatement(sql)
          (1 to params.size) foreach { i =>
            stmt.setInt(i, params(i - 1).toString.toInt)
          }

          val rs = stmt.executeQuery()
          conn.close()
        case None =>
      }
    }

    time("EntityManager", test_count) {
      sinq_h2.withEntityManager { em =>
        val query = em.createNativeQuery(sql, classOf[Student])
        (1 to params.size) foreach { i =>
          query.setParameter(i, params(i - 1))
        }
        buffer += query.getSingleResult
      }
    }
    println(s"size:${buffer.size} head:${buffer(0).toString()}")
    buffer.clear()
  }

  private def time(name: String, count: Int)(call: => Unit): Unit = {
    val start = System.currentTimeMillis()

    (0 to count).toParArray.foreach { i => call }

    val end = System.currentTimeMillis()
    println(s"##$name## time:${end - start}ms.")
  }

  private def jdbc(): Option[Connection] = {
    val driver = "org.h2.Driver"
    val user = "sa"
    //val url = "jdbc:h2:tcp://127.0.0.1:9999/~/test"
    val url = "jdbc:h2:mem:test"
    val pass = ""
    try {
      Class.forName(driver)
      Some(DriverManager.getConnection(url, user, pass))
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }
}

