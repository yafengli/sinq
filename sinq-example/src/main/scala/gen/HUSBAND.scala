package gen

import io.sinq._

object HUSBAND extends Table[models.Husband]("t_husband"){
    def name = Column(this,classOf[String],"name")
    def age = Column(this,classOf[Int],"age")
    def id = Column(this,classOf[BigInt],"id")
    def teacher = Column(this,classOf[BigInt],"teacher")

    def * = Seq(name,age,id,teacher)
}