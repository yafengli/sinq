package test

import org.koala.jporm.jpa.{JpormFacade, JpaFactory}

import org.specs2._
import java.util
import models.{Teacher, Student}

/**
 * User: ya_feng_li@163.com
 * Date: 13-4-22
 * Time: 下午4:05
 */
class JpormSpec extends mutable.Specification {

  "Init Data" should {
    "Init" in {
      test()
    }
  }

  def test() {
    /**
    JpaFactory.bind("default")
    val facade = new JpormFacade("default")
    val entity = new Student()
    entity.address = "Hello World!"
    entity.age = 111
    entity.name = "test"
    entity.teacher = facade.get(1L, classOf[Teacher])
    facade.insert(entity)

    val list = new util.ArrayList[AnyRef]()
    list.add(Integer.valueOf(7))
    val count = facade.count("student.n_find_m", list)
    val stus = facade.fetch(10, 0, "find_ok", list, classOf[Student])
    println(f"#count:${count} ${stus}")

    val objs = facade.fetch(10, 0, "student.n_find_mt", list)
    objs.toArray.foreach {
      it =>
        if(it.isInstanceOf[Array[_]]){
          it.asInstanceOf[Array[_]].foreach {
            obj =>
              println(f"#${obj.getClass} ${obj.toString.toLong}")
          }
        }
    }

    val single=facade.single("student.n_find_mt",list)
    single.foreach {
      obj =>
        println(f"@@${obj.getClass} ${obj.toString.toLong}")
    }
      */
  }
}

trait SomeTrait[A] {
  def foo(implicit ev: Manifest[A]) = {
    (ev.runtimeClass)
  }
}