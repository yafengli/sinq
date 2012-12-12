import models.{Book, Husband, Teacher, Student}
import org.koala.sporm.jpa.{SpormFacade, JPA}
import org.specs2.mutable.Specification

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBSpec extends Specification {
  val init = {
    JPA.initPersistenceName("default")
    println("#emf:" + JPA.lookEntityManagerFactory())
    Unit
  }

  "Test JPA." should {
    "store test data and casecade" in {
      time(() => {
        val t = Teacher.get(1L)
        if (!t.isDefined) {
          //teacher
          val teacher = new Teacher("LYF", 28, "NanJing")
          teacher.insert()
          //teacher husband
          val husband = new Husband("LYF", 28, teacher)
          husband.insert()

          //student
          val st_1 = new Student("student_1", 9, "GongZhuLing", teacher)
          val st_2 = new Student("student_2", 7, "GongZhuLing", teacher)
          st_1.insert()
          st_2.insert()
          //book
          val book_1 = new Book("SB i", 12, st_1)
          val book_2 = new Book("SB ii", 12, st_1)

          book_1.insert()
          book_2.insert()
        }
      })
    }
    "criteria builder test" in {
      time(() => {
        Student.withEntityManager {
          em => {
            //            val list = CriteriaQL(em, classOf[Student]).fetch(10, 1)
            /*
            list.map {
              it =>
                println(it.id + "@" + it.name + "@" + it.address)
            }
            */
          }
        }
      })
    }

    "sporm test" in {
      time(() => {
        val sporm = SpormFacade()
        sporm.get(classOf[Student], 1L)
      })
    }
  }

  def time(f: () => Unit)() {
    val start = System.currentTimeMillis()
    f()
    val stop = System.currentTimeMillis()
    println("-------#time use " + (stop - start) + "ms.")
  }
}
