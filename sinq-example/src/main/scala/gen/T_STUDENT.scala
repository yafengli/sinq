package gen

import io.sinq.provider._

object T_STUDENT extends Table[models.Student]("e_student"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def address = Column(this,classOf[String],"address")
    def id = Column(this,classOf[java.math.BigInteger],"id")
    def teacher = Column(this,classOf[java.math.BigInteger],"teacher_id")
}