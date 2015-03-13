package org.koala.sporm.rs

import scala.beans.BeanProperty
import scala.collection.mutable

trait ConditionII {

  import org.koala.sporm.rs.ConditionII._

  @BeanProperty
  var from: ConditionII = _

  @BeanProperty
  val linkCache = new StringBuffer()

  @BeanProperty
  var flag: String = _
  @BeanProperty
  var root: ConditionII = _
  lazy val buffer = new StringBuffer()
  lazy val params = mutable.ArrayBuffer[Any]()
  lazy val to = mutable.ArrayBuffer[ConditionII]()

  def and(link: ConditionII): ConditionII = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(AND)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def or(link: ConditionII): ConditionII = {
    if (this.getFrom == null) this.setRoot(this) else this.setRoot(this.getFrom.getRoot)
    link.setFlag(OR)
    nodeInit(link, this.getRoot)
    this.getRoot
  }

  def toSql(): String = {
    endLoop(this)
    this.getRoot.buffer.toString
  }

  private def endLoop(link: ConditionII): Unit = {
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

  protected def nodeInit(link: ConditionII, root: ConditionII): Unit = {
    link.setFrom(this)
    nodeUp(link, root)
    nodeDown(link, root)
    this.to += link
  }

  protected def nodeUp(link: ConditionII, root: ConditionII): Unit = {
    link.setRoot(root)
    if (link.getFrom != null) nodeUp(link.getFrom, root)
  }

  protected def nodeDown(link: ConditionII, root: ConditionII): Unit = {
    link.setRoot(root)
    link.to.foreach(nodeDown(_, root))
  }
}

object ConditionII {
  val AND = " and "
  val OR = " or "
  val START_BRACKET = "("
  val END_BRACKET = ")"
}

