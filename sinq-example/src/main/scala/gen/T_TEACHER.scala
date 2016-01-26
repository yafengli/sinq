package gen

import io.sinq.provider._

object T_TEACHER extends Table[models.Teacher]("e_teacher"){
    def name = column("name", classOf[String])
    def age = column("age", classOf[Int])
    def address = column("address", classOf[String])
    def id = column("id", classOf[java.math.BigInteger])
}