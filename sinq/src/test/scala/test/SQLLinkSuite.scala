package test

import io.sinq.SinqIIStream
import io.sinq.expression._
import io.sinq.rs.{ASC, Column, Order, Table}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SQLLinkSuite extends FunSuite with BeforeAndAfter {
  before {
  }

  after {
  }

  test("SINQ II SQL.") {
    val cd = Ge(Column("name"), Seq(1)).and(Le(Column("age"), Seq(2)).or(Ge(Column("id"), Seq("3")))).or(Ge(Column("address"), Seq(4)).and(Le(Column("email"), Seq("5"))))

    val sinq = SinqIIStream()
    val _table = Table("t_student", "t")
    val columns = Column(_table, "id", "name")
    val query = sinq.select(columns: _*).from(_table).where(cd).groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

    line({ () => println(cd.translate())}, 2)
    line({ () => cd.params.foreach(println(_))}, 2)
    line({ () => println(query.sql())}, 2)
    line({ () => query.params().foreach(println(_))}, 2)

    def line(call: () => Unit, count: Int): Unit = {
      def split(): Unit = {
        0 to 100 foreach (i => print("#"))
        println("")
      }
      (0 until count).foreach(i => {
        call()
        split()
      })
    }
  }

  test("SINQ II QUERY.") {
    val cd = Eq(Column("name"), Seq("YaFengLi")).and(Ge(Column("age"), Seq(11)).or(Ge(Column("id"), Seq(-1))).or(In(Column("id"), Seq(1L, 2L, 3L))))

    val sinq = SinqIIStream()
    val _table = Table("t_student", "t")
    val columns = Column(_table, "id", "name")
    val query = sinq.select(columns: _*).from(_table).where(cd).groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

    query.single() match {
      case Array(id, name) => println(s"id:${id} name:${name}")
      case _ => println("None")
    }
  }
}


