package gen

import io.sinq.provider._

object T_USER extends Table[models.User]("e_user"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def id = Column(this,classOf[java.math.BigInteger],"id")
}