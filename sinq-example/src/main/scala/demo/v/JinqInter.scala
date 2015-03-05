package demo.v

import demo.models.User
import org.jinq.jpa.JinqJPAScalaIteratorProvider
import org.koala.sporm.jpa.JPA

object JinqInter extends JPA {
  val streams = new JinqJPAScalaIteratorProvider(JPA.entityManagerFactory().get)


}
