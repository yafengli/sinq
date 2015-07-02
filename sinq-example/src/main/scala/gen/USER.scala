package gen

import io.sinq._

object USER extends Table[models.User]("t_user"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def id = Column(this,classOf[java.math.BigInteger],"id")

    def * = Seq(name,age,id)
}