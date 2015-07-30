package gen

import io.sinq._

object _STUDENT extends Table[models.Student]("t_student"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def address = Column(this,classOf[String],"address")
    def id = Column(this,classOf[java.math.BigInteger],"id")
    def teacher = Column(this,classOf[java.math.BigInteger],"teacher_id")
}