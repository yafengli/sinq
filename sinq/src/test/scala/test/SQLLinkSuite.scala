package test

import io.sinq.SinqIIStream
import io.sinq.expression._
import io.sinq.rs.{ASC, Column, Order, Table}
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

  val single_cd = Eq(_id, "3").and(Le(_id, 12).or(Ge(_age, 11L).and(In(_id, Seq(1, 2, 3))))).or(Ge(_age, 15L))


  val col_cd = Between(_id, -1, 12).and(Ge(_age, 11L).or(In(_id, Seq(1, 2, 3, 4, 5))))

  before {
    H2DB.open
  }

  after {
    H2DB.close
  }

  test("SINQ II SQL Build.") {
    (0 to 2).foreach {
      i =>
        println(single_cd.translate())
        single_cd.params.foreach(println(_))
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

  test("Single.") {
    val query = sinq.select(_all: _*).from(_table).where(single_cd) //.groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)
    query.single() match {
      case Array(id, name) => println(s"id:${id} name:${name}")
      case _ => println("None")
    }
  }
  test("Collect.") {
    val query = sinq.select(_all: _*).from(_table).where(col_cd).orderBy(Order(ASC, _all: _*)).limit(10, 0) //.groupBy(columns: _*)
    query.collect(classOf[Array[Any]]).foreach {
      case Array(id, name) => println(s"#id:${id} name:${name}")
      case _ =>
    }
  }
}


