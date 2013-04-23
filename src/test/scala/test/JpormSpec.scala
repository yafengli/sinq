package test

import org.koala.jporm.jpa.{JpormFacade, JpaFactory}

import org.specs2._
import java.util
import models.Student

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
    JpaFactory.bind("default")
    val facade = new JpormFacade("default")
    val list = new util.ArrayList[AnyRef]()
    list.add(Integer.valueOf(7))
    val count = facade.count("student.n_find_m", list)
    val stus = facade.fetch(10, 0, "find_ok", list, classOf[Student])
    println(f"#count:${count} ${stus}")
  }
}
