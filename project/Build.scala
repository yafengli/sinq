import java.io.File
import scala.io.Source

object Build{
	 val reg = "(.+)=(.+)".r
  val $ = Source.fromFile(new File("project/build.properties"))
    .getLines().filter(reg.findFirstMatchIn(_).isDefined).map(reg.findFirstMatchIn(_).get).map(m => (m.group(1) -> m.group(2))).toMap
}