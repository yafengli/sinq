package jpa

import java.util.stream.Stream
import jpa.entity.User
import jpa.impl._
import jpa.util.JPA

object DemoJpa extends App {
  JPA.init("h2")
  lazy val ua = ActiveJPA[User]("h2")

  println("Hello World!")
  println(s"${ua.getClass().getName}")
  println(s"type:${ua.getType}")

  val stream = Stream.of(1,2,3,4,5)

  dbStore(ua)
  find(ua, 36L)
  fetch(ua)

  def fetch(a: ActiveJPA[User]): Unit = {
    a.fetch("age").foreach { u =>
      println(s"id:${u.id} name:${u.name} age:${u.age}")
    }
  }

  def find(a: ActiveJPA[User], id: Long): User = {
    val user = if (a.exists(id)) a.find(id) else new User()

    if (user.id != id) {
      user.setName("YaFengLi")
      user.setAge(33)
      ua.save(user)
    }
    println(s"id:${user.id} name:${user.name} age:${user.age}")
    user
  }

  def dbStore(a: ActiveJPA[User]): Unit = {
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
