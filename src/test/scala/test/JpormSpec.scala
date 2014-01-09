package test

import models.{Teacher, Student}
import org.koala.jporm.jpa.JpormFacade
import org.specs2.ScalaCheck
import org.specs2.mutable._
import scala.collection.JavaConversions._

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午4:05
 */
class JpormSpec extends Specification {

  override def is = s2"""
  one ${}
  one ${}
  """

  case class steps() extends ScalaCheck {
    def test() {
      val facade = new JpormFacade("default")
      val entity = new Student()
      entity.address = "Hello World!"
      entity.age = 111
      entity.name = "test"
      entity.teacher = facade.get(1L, classOf[Teacher])
      facade.insert(entity)

      val params = Map("age" -> Integer.valueOf(7))
      val count = facade.count("student.n_find_m", params)
      val stus = facade.fetch(classOf[Student], 10, 0, "find_ok", params)
      println(f"#count:${count} ${stus}")

      val objs = facade.fetch(10, 0, "student.n_find_mt", params)
      objs.toArray.foreach {
        it =>
          if (it.isInstanceOf[Array[_]]) {
            it.asInstanceOf[Array[_]].foreach {
              obj =>
                println(f"#${obj.getClass} ${obj.toString.toLong}")
            }
          }
      }

      val single = facade.single("student.n_find_mt", params)
      single.foreach {
        obj =>
          println(f"@@${obj.getClass} ${obj.toString.toLong}")
      }
    }
  }

}

trait SomeTrait[A] {
  def foo(implicit ev: Manifest[A]) = {
    (ev.runtimeClass)
  }
}