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
    H2DB.open
  }

  after {
    H2DB.close
  }

  test("SINQ II SQL.") {
    val sinq = SinqIIStream()

    val _table = Table("t_student", "t")
    val columns = Column(_table, "id", "name")
    val cd = Eq(Column(_table, "name"), Seq("1")).and(Ge(Column(_table, "age"), Seq(2)).or(Ge(Column(_table, "id"), Seq(3))).or(In(Column(_table, "id"), Seq(4l))))

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
    val sinq = SinqIIStream()

    val _table = Table("t_student", "t")
    val columns = Column(_table, "id", "name")
    val cd = Eq(Column(_table, "name"), Seq("YaFengLi")).and(Ge(Column(_table, "age"), Seq(11)).or(Ge(Column(_table, "id"), Seq(-1))))//.or(Le(Column(_table, "id"), Seq(5L))))

    val query = sinq.select(columns: _*).from(_table).where(cd)//.groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

    query.single() match {
      case Array(id, name) => println(s"id:${id} name:${name}")
      case _ => println("None")
    }
  }
}


