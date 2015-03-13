package demo.v

import io.sinq.SinqIIStream
import io.sinq.expression.{Ge, Le}
import io.sinq.rs._

object DemoSinqStream extends App {

  val cd = Ge(Column("name"), Seq(1)).and(Le(Column("age"), Seq(2)).or(Ge(Column("id"), Seq("3")))).or(Ge(Column("address"), Seq(4)).and(Le(Column("email"), Seq("5"))))
  println(cd.translate())
  line()
  cd.params.foreach(println(_))
  line()
  println(cd.translate())
  line()
  cd.params.foreach(println(_))
  line()
  val sinq = SinqIIStream()
  val _table = Table("t_student", "t")
  val columns = Column(_table, "id", "name")
  val query = sinq.select(columns: _*).from(_table).where(cd).groupBy(columns: _*).orderBy(Order(ASC, columns: _*)).limit(10, 5)

  println(query.sql())
  line()
  println(query.sql())
  line()
  println(query.sql())
  line()
  query.params().foreach(println(_))
  line()

  query.params().foreach(println(_))
  line()
  query.params().foreach(println(_))
  line()

  def line(): Unit = {
    0 to 40 foreach (i => print("#"))
    println("")

  }
}