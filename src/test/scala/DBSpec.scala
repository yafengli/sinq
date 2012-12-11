import org.specs2.mutable.Specification
import org.sporm.jpa.JPA

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
class DBSpec extends Specification {
  "Test JPA." should {
    "init" in {
      JPA.initPersistenceName("default")
      println(JPA.lookEntityManagerFactory())
    }
  }
}
