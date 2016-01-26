package gen

import io.sinq.provider._

object T_HUSBAND extends Table[models.Husband]("e_husband"){
    def name = column("name", classOf[String])
    def age = column("age", classOf[Int])
    def id = column("id", classOf[java.math.BigInteger])
    def teacher = column("teacher", classOf[java.math.BigInteger])
}