package gen

import io.sinq._

object ADDRESS extends Table[models.Address]("t_address"){
    def name = Column(this,classOf[String],"name")
    def num = Column(this,classOf[Int],"num")
    def id = Column(this,classOf[java.math.BigInteger],"id")
    def user = Column(this,classOf[java.math.BigInteger],"u_id")

    def * = Seq(name,num,id,user)
}