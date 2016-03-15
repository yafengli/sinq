package test

import java.sql.{Connection, DriverManager}

import gen.{T_STUDENT, T_TEACHER}
import io.sinq.builder.ConditionBuilder
import io.sinq.expr.{Eq, Ge, Le}
import io.sinq.func.ASC
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DB._

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
    val test_count = 1000
    val condition = Eq(T_STUDENT.id, 1).or(Le(T_STUDENT.id, 9L).and(Ge(T_STUDENT.age, 11L).and(Le(T_STUDENT.age, 15L))))
    val cb = ConditionBuilder()
    val params = cb.params(condition)
    println(s"sql:${cb.translate(condition)}")
    val start = System.currentTimeMillis()
    println(s"sinq:${sinq_h2}")
    val end = System.currentTimeMillis()
    println(s"time:${end - start}ms.")

    time("from", test_count) {
      sinq_h2.from(T_STUDENT)
    }
    val from = sinq_h2.from(T_STUDENT)
    time("join", test_count) {
      from.join(T_TEACHER)
    }

    val join = from.join(T_TEACHER)
    time("on", test_count) {
      join.on(Eq(T_STUDENT.teacher, T_TEACHER.id))
    }

    val on = join.on(Eq(T_STUDENT.teacher, T_TEACHER.id))
    time("where", test_count) {
      on.where(condition)
    }

    val where = on.where(condition)
    time("orderBy", test_count) {
      where.orderBy(ASC(T_STUDENT.id))
    }

    val orderBy = where.orderBy(ASC(T_STUDENT.id))
    time("limit", test_count) {
      orderBy.limit(10, 0)
    }
    val query = orderBy.limit(10, 0)
    println(s"sql:${query.sql()}")
    println(s"params:${query.params()}")

    val q = sinq_h2.from(T_STUDENT).join(T_TEACHER).on(Eq(T_STUDENT.teacher, T_TEACHER.id)).where(condition).orderBy(ASC(T_STUDENT.id)).limit(10, 0)
    time("sql", test_count) {
      //q.sql()
      //sinq_h2.from(T_STUDENT).join(T_TEACHER).on(Eq(T_STUDENT.teacher, T_TEACHER.id)).where(condition).orderBy(ASC(T_STUDENT.id)).limit(10, 0).params()
      q.single()
    }

    val sql = "select t1.* from e_student t1 inner join e_teacher t2 on t1.teacher_id = t2.id " +
      "where t1.id = ? or (t1.id <= ? and (t1.age >= ? and t1.age <= ?)) order by t1.id ASC limit 10 offset 0"

    jdbc() match {
      case Some(conn) =>
        println(s"conn:${conn}")

        time("jdbc", test_count) {

          val stmt = conn.prepareStatement(sql)
          (1 to params.size) foreach { i =>
            stmt.setInt(i, params(i - 1).toString.toInt)
          }

          val rs = stmt.executeQuery()
        }
        conn.close()
      case None =>
    }

    time("EntityManager", test_count) {
      sinq_h2.withEntityManager { em =>
        val query = em.createNativeQuery(sql)
        (1 to params.size) foreach { i =>
          query.setParameter(i, params(i - 1))
        }
        query.getResultList
      }
    }
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
    val url = "jdbc:h2:tcp://127.0.0.1:9999/~/test"
    val pass = ""
    try {
      Class.forName(driver)
      Some(DriverManager.getConnection(url, user, pass))
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }
}

