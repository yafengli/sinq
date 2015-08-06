package gen

import io.sinq.provider.{Table, Column}

object _USER extends Table[models.User]("t_user"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def id = Column(this,classOf[java.math.BigInteger],"id")
}