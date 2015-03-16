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
    val cd = Eq(Column(_table, "name"), "1").or(Ge(Column(_table, "age"), 2).or(Ge(Column(_table, "id"), 3).or(In(Column(_table, "id"), Seq(40L, 41L, 42L)))))

    val query = sinq.select(columns: _*).from(_table).where(cd).groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

    (0 to 2).foreach {
      i =>
        println(cd.translate())
        cd.params.foreach(println(_))
        println("####################################")
    }
  }

  test("SINQ II QUERY.") {
    val sinq = SinqIIStream()
    val _table = Table("t_student", "t")
    val columns = Column(_table, "id", "name")
    val cd = Eq(Column(_table, "id"), "2").or(In(Column(_table, "id"), Seq(1, 2, 3)))

    val query = sinq.select(columns: _*).from(_table).where(cd) //.groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

    (0 to 2).foreach {
      i =>
        println(cd.translate())
        cd.params.foreach(println(_))
        println("####################################")
    }

    query.single() match {
      case Array(id, name) => println(s"id:${id} name:${name}")
      case _ => println("None")
    }
  }
}


