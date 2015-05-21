package gen

import io.sinq._

object STUDENT extends Table[models.Student]("t_student"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def address = Column(this,classOf[String],"address")
    def id = Column(this,classOf[BigInt],"id")
    def teacher = Column(this,classOf[BigInt],"teacher_id")

    def * = Seq(name,age,address,id,teacher)
}