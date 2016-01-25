package test

import gen.T_STUDENT
import io.sinq.expr._
import models.Student
import test.H2DB._

object SingleUnit {
  def test(): Unit = {
    val query = sinq.select(T_STUDENT.id, T_STUDENT.name, T_STUDENT.age).from(T_STUDENT).where(Eq(T_STUDENT.id, 1))
    query.single() match {
      case Some((id, name, age)) => println(s"#id:${id} name:${name} age:${age}")
      case None => println("None")
    }
    sinq.find(1L, classOf[Student]) match {
      case Some(s) => println(s">id:${s.id} name:${s.name} age:${s.age}")
      case None =>
    }
  }
}

