package gen

import io.sinq.provider._

object T_USER extends Table[models.User]("e_user"){
    def name = column("name", classOf[String])
    def age = column("age", classOf[Int])
    def id = column("id", classOf[java.math.BigInteger])
}