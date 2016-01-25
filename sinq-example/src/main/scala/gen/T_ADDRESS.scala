package gen

import io.sinq.provider._

object T_ADDRESS extends Table[models.Address]("e_address"){
    def name = Column(this,classOf[String],"name")
    def num = Column(this,classOf[Int],"num")
    def id = Column(this,classOf[java.math.BigInteger],"id")
    def user = Column(this,classOf[java.math.BigInteger],"u_id")
}