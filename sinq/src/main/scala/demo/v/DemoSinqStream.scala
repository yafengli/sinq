package demo.v

import org.koala.sporm.SinqIIStream
import org.koala.sporm.rs._

object DemoSinqStream extends App {

  val cd = Ge(Column("name"), Seq(1)).and(Le(Column("age"), Seq(2)).or(Ge(Column("id"), Seq("3")))).or(Ge(Column("address"), Seq(4)).and(Le(Column("email"), Seq("5"))))
  println(cd.translate())

  cd.params.foreach(println(_))
  println(cd.translate())
  cd.params.foreach(println(_))
  val sinq = SinqIIStream()
  val _table = Table("t_student", "t")
  val columns = Column(_table, "id", "name")
  val query = sinq.select(columns: _*).from(_table).where(cd).groupBy(columns: _*).orderBy(Order("ASC", columns: _*)).limit(10, 5)

  println(query.sql())
}