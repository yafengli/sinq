package gen

import io.sinq.provider._

object T_ADDRESS extends Table[models.Address]("e_address"){
    def name = column("name", classOf[String])
    def num = column("num", classOf[Int])
    def id = column("id", classOf[java.math.BigInteger])
    def user = column("u_id", classOf[java.math.BigInteger])
}