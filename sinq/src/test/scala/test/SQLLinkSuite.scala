package test

import io.sinq.SinqIIStream
import io.sinq.expression.{Ge, Le}
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

  test("ConditionII SQL.") {
    val cd = Ge(Column("name"), Seq(1)).and(Le(Column("age"), Seq(2)).or(Ge(Column("id"), Seq("3")))).or(Ge(Column("address"), Seq(4)).and(Le(Column("email"), Seq("5"))))

    val sinq = SinqIIStream()
    val _table = Table("t_student", "t")
    val columns = Column(_table, "id", "name")
    val query = sinq.select(columns: _*).from(_table).where(cd).groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

    line({ () => println(cd.translate())}, 2)
    line({ () => cd.params.foreach(println(_))}, 2)
    line({ () => println(query.sql())}, 3)
    line({ () => query.params().foreach(println(_))}, 3)

    def line(call: () => Unit, count: Int): Unit = {
      def split(): Unit = {
        0 to 100 foreach (i => print("#"))
        println("")
      }
      (0 to count).foreach(i => {
        call()
        split()
      })
    }
  }
}


