package test

import jpa.entity.User
import jpa.impl.ActiveJPA
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import test.H2DBInit._

@RunWith(classOf[JUnitRunner])
class ActiveSuite extends FunSuite with BeforeAndAfter {

  before {
    init()
  }

  after {
    latch.countDown()
  }

  test("Active JPA") {
    dbStore
    find(36L)
    fetch

    def fetch(implicit a: ActiveJPA[User]): Unit = {
      a.fetch("age").foreach { u =>
        println(s"id:${u.id} name:${u.name} age:${u.age}")
      }
    }

    def find(id: Long)(implicit a: ActiveJPA[User]): User = {
      val user = if (a.exists(id)) a.find(id) else new User()

      if (user.id != id) {
        user.setName("YaFengLi")
        user.setAge(33)
        a.save(user)
      }
      println(s"id:${user.id} name:${user.name} age:${user.age}")
      user
    }

    def dbStore(implicit a: ActiveJPA[User]): Unit = {
      if (a.count() <= 10) {
        (0 to 10).foreach { i =>
          val user = new User()
          user.setName("Name" + i)
          user.setAge(i)
          a.save(user)
        }
      }
    }
  }
}

