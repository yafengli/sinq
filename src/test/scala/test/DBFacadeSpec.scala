package test

import models._
import org.koala.sporm.jpa.SpormFacade
import org.specs2._

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBFacadeSpec extends mutable.Specification {

  val facade = SpormFacade("default")

  "SpormFacade test all" should {

    "Sporm test" in {
      test
    }
  }

  def test = {
    time(() => {
      facade.get(classOf[Student], 1L)
      "Sporm test"
    })
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println("---%s--#time use %sms.".format(name, stop - start))
  }
}