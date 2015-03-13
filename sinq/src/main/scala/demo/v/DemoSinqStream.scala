package demo.v

import org.koala.sporm.rs.{Column, Ge, Le}

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

  lazy val buffer = new StringBuffer()
  lazy val params = mutable.ArrayBuffer[Any]()
  lazy val to = mutable.ArrayBuffer[Link]()

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
    endLoop(this)
    this.getRoot.buffer.toString
  }

  private def endLoop(link: Link): Unit = {
    if (link.getFrom != null) this.getRoot.buffer.append(link.getFlag)
    if (link.getFrom != null && link.to.nonEmpty) {
      this.getRoot.buffer.append(START_BRACKET)
      this.getRoot.buffer.append(link.rule())
    }
    else this.getRoot.buffer.append(link.rule())

    if (link.to.isEmpty) this.getRoot.buffer.append(END_BRACKET)

    link.to.foreach(endLoop(_))
  }

  protected def rule(): String = {
    values.foreach(this.getRoot.params += _)
    alias()
  }

  def values: Seq[Any]

  def alias(): String

  protected def nodeInit(link: Link, root: Link): Unit = {
    link.setFrom(this)
    nodeUp(link, root)
    nodeDown(link, root)
    this.to += link
  }

  protected def nodeUp(link: Link, root: Link): Unit = {
    link.setRoot(root)
    if (link.getFrom != null) nodeUp(link.getFrom, root)
  }

  protected def nodeDown(link: Link, root: Link): Unit = {
    link.setRoot(root)
    link.to.foreach(nodeDown(_, root))
  }
}

case class L1(val col: String, val values: Seq[Any]) extends Link {
  override def alias(): String = s"${col} = ?"
}

case class L2(val col: String, val values: Seq[Any]) extends Link {
  override def alias(): String = s"${col} >= ?"
}

trait Hello {
  def name: String
}

case class HelloImpl(val name: String) extends Hello

object BootLink extends App {
  val link = L1("name", Seq("1")).and(L1("age", Seq(2)).or(L2("id", "3"))).or(L1("address", "4").and(L2("email", "5")))
  println(s"::${link.toSql()}::")
  link.params.foreach(println(_))

  val cd = Ge(Column("name"), Seq(1)).and(Le(Column("age"), Seq(2)).or(Ge(Column("id"), Seq("3")))).or(Ge(Column("address"), Seq(4)).and(Le(Column("email"), Seq("5"))))
  println(cd.toSql())
  cd.params.foreach(println(_))
}