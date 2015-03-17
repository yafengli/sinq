package test

import demo.models.User
import io.sinq.provider.JPA
import org.jinq.jpa.{JPQL, JinqJPAScalaIteratorProvider}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class JinqScalaSuite extends FunSuite with BeforeAndAfter with JPA {
  before {
    init.H2DB.open
  }

  after {
    init.H2DB.close
  }
  test("Jinq Scala And") {
    val streams = new JinqJPAScalaIteratorProvider(JPA.entityManagerFactory().get)
    withEntityManager {
      em =>
        streams.streamAll(em, classOf[User]).where(u => u.getAge > 10 && u.getName == "hello").toList
    }
  }

  test("Jinq Scala In") {
    val streams = new JinqJPAScalaIteratorProvider(JPA.entityManagerFactory().get)
    withEntityManager {
      em =>
        streams.streamAll(em, classOf[User]).where(u => JPQL.isInList(u.getName, Seq("one", "two"))).toList
    }
  }
}
