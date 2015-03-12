package demo.v

import scala.beans.BeanProperty

abstract class SayHi[T: Manifest] {
  def sayHi(msg: String): Unit = println(s"#${Thread.currentThread().getId}#Hi ${msg}")
}

object DemoSinqStream {

  implicit def stream[T: Manifest](t: T) = new SayHi[T] {}
}

trait Link {

  import org.koala.sporm.rs.ConditionII._

  import scala.collection.mutable

  @BeanProperty
  var from: Link = _

  @BeanProperty
  var root: Link = _

  @BeanProperty
  var flag: String = _


  @BeanProperty
  var buffer = new StringBuffer()

  @BeanProperty
  var params = mutable.ArrayBuffer[Any]()

  val to = mutable.ArrayBuffer[Link]()

  def and(link: Link): Link = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(AND)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def or(link: Link): Link = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(OR)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def toSql(): String = {
    if (this.getFrom != null) {
      this.setBuffer(this.getFrom.getBuffer)
      this.setParams(this.getFrom.getParams)
    }
    endLoop(this, this.getBuffer)
    this.getBuffer.toString
  }

  private def endLoop(link: Link, buffer: StringBuffer): Unit = {
    if (link.getFrom != null) buffer.append(link.getFlag)
    if (link.getFrom != null && link.to.nonEmpty) {
      buffer.append(START_BRACKET)
      buffer.append(link.alias())
    }
    else buffer.append(link.alias())

    if (link.to.isEmpty) buffer.append(END_BRACKET)

    link.to.foreach(endLoop(_, buffer))
  }

  protected def store(values: Any*): Unit = {
    println(this.getRoot)
    values.foreach(this.getRoot.getParams += _)
  }

  def alias(): String

  protected def nodeInit(link: Link, root: Link): Unit = {
    link.setFrom(this)
    rootNode(link, root)
    this.to += link
  }

  protected def rootNode(link: Link, root: Link): Unit = {
    link.setRoot(root)
    println("rn:" + link + " :" + root)
    if (link.getFrom != null) {
      link.getFrom.setRoot(root)
      rootNode(link.getFrom, root)
    }
  }
}

case class L1(val col: String, val value: Any) extends Link {
  override def alias(): String = {
    store(value)
    s"${col} = ?"
  }
}

case class L2(val col: String, val value: Any) extends Link {
  override def alias(): String = {
    store(value)
    s"${col} >= ?"
  }
}

object BootLink extends App {
  val link = L1("name", "1").and(L1("age", 2).or(L2("id", 3))).or(L1("address", 4).and(L2("email", "5")))
  println(s"::${link.toSql()}::")
  link.getParams.foreach(println(_))
}