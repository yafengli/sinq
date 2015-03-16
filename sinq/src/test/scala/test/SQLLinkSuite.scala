package test

import io.sinq.SinqIIStream
import io.sinq.expression._
import io.sinq.rs.{Column, Table}
import models.Student
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class SQLLinkSuite extends FunSuite with BeforeAndAfter {
  val sinq = SinqIIStream()
  val _table = Table("t_student", "t")
  val _id = Column(_table, "id")
  val _name = Column(_table, "name")
  val _age = Column(_table, "age")
  val _all = Column(_table, "id", "name")

  val cd = Eq(_id, "3").and(Le(_id, 12).or(Ge(_age, 11L).and(In(_id, Seq(1, 2, 3))))).or(Ge(_age, 15L))

  before {
    H2DB.open
  }

  after {
    H2DB.close
  }

  test("SINQ II SQL Build.") {
    (0 to 2).foreach {
      i =>
        println(cd.translate())
        cd.params.foreach(println(_))
        println("####################################")
    }
  }

  test("DB Init.") {
    val count = sinq.count(classOf[Student])
    if (count <= 0) {
      sinq.insert(Student("YaFengli", 999, "NanJing"))
    }

    println(s"count:${count}")
    sinq.select().from(_table).collect(classOf[Student]).foreach(s => println(s"id:${s.id}"))
  }

  test("SINQ II QUERY.") {
    val query = sinq.select(_all: _*).from(_table).where(cd) //.groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)
    query.single() match {
      case Array(id, name) => println(s"id:${id} name:${name}")
      case _ => println("None")
    }
  }
}


