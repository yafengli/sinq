import javax.persistence.criteria.Predicate
import models.{Book, Husband, Teacher, Student}
import org.koala.sporm.jpa.{CriteriaQL, SpormFacade, JPA}
import org.specs2._

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBSpec extends mutable.Specification {
  val init = {
    JPA.initPersistenceName("default")
    println("#emf:" + JPA.lookEntityManagerFactory())

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
  }

  "Test all" should {
    "d2" in {
      d2
    }
    "d1" in {
      d1
    }
  }

  def d1 = {
    time(() => {
      Student.withEntityManager {
        em => {
          CriteriaQL(em, classOf[Student]).fetch(10, 1)
          CriteriaQL(em, classOf[Book]).and(cq => {
            val list = List[Predicate]()

            list
          }).fetch()
        }
      }
      "d1"
    })
  }

  def d2 = {
    time(() => {
      val sporm = SpormFacade()
      sporm.get(classOf[Student], 1L)
      "d2"
    })
  }

  def time(f: () => String)() {
    val start = System.currentTimeMillis()
    val name = f()
    val stop = System.currentTimeMillis()
    println("---%s--#time use %sms.".format(name, stop - start))
  }
}
