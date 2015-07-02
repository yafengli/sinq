package gen

import io.sinq._

object TEACHER extends Table[models.Teacher]("t_teacher"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def address = Column(this,classOf[String],"address")
    def id = Column(this,classOf[java.math.BigInteger],"id")

    def * = Seq(name,age,address,id)
}