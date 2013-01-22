package test

import models._
import org.specs2._
import org.koala.sporm.jpa.JPA

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBInitSpec extends mutable.Specification {
  "Init Data" should {
    "Init" in {
      JPA.bind("default")

      val t = Teacher.get(1L)
      if (!t.isDefined) {
        val teacher = new Teacher("LYF", 28, "NanJing")
        teacher.insert()

        val husband = new Husband("LYF", 28, teacher)
        husband.insert()

        val st_1 = new Student("student_1", 9, "GongZhuLing", teacher)
        val st_2 = new Student("student_2", 7, "GongZhuLing", teacher)
        st_1.insert()
        st_2.insert()

        val book_1 = new Book("SB i", 12, st_1)
        val book_2 = new Book("SB ii", 12, st_1)

        book_1.insert()
        book_2.insert()
      }
    }
  }
}
