package gen

import io.sinq.provider._

object T_TEACHER extends Table[models.Teacher]("e_teacher"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def address = Column(this,classOf[String],"address")
    def id = Column(this,classOf[java.math.BigInteger],"id")
}