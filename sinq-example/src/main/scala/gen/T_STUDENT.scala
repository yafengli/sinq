package gen

import io.sinq.provider._

object T_STUDENT extends Table[models.Student]("e_student"){
    def name = column("name", classOf[String])
    def age = column("age", classOf[Int])
    def address = column("address", classOf[String])
    def id = column("id", classOf[java.math.BigInteger])
    def teacher = column("teacher_id", classOf[java.math.BigInteger])
}