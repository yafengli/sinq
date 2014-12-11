import java.io.File
import scala.io.Source

object Build{
	val reg = "(.+)=(.+)".r
	lazy val $ = scala.io.Source.fromFile(new File("project/build.properties")).getLines().map(it => {
  	val m = reg.findFirstMatchIn(it).get
  	(m.group(1).trim, m.group(2).trim)
	}).toMap
}