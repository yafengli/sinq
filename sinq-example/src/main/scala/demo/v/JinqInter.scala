package demo.v

import demo.models.User
import io.sinq.provider.JPA
import org.jinq.jpa.JinqJPAScalaIteratorProvider

object JinqInter extends JPA {
  val streams = new JinqJPAScalaIteratorProvider(JPA.entityManagerFactory().get)


}
